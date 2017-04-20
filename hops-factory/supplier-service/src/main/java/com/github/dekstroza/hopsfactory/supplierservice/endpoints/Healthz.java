package com.github.dekstroza.hopsfactory.supplierservice.endpoints;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.SERVICE_UNAVAILABLE;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dekstroza.hopsfactory.supplierservice.util.ExposeLogControl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/healthz", description = "Supplier service healthcheck endpoint.")
@RequestScoped
@Path("healthz")
public class Healthz implements ExposeLogControl {

    @Resource(lookup = "jboss/datasources/SupplierDS")
    private DataSource dataSource;

    private static final Logger log = LoggerFactory.getLogger(Healthz.class);

    @ApiOperation(httpMethod = "GET", value = "Perform healt hcheck operation.", produces = TEXT_PLAIN, consumes = TEXT_PLAIN)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 503, message = "Health check failed.") })
    @GET
    @Consumes(TEXT_PLAIN )
    @Produces(TEXT_PLAIN)
    public Response basicHealthcheck() {

        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(1)) {
                log.info("Healthcheck is returning OK.");
                return status(OK).entity("OK").build();
            } else {
                log.error("Healthcheck failed to return valid connection within 1 second.");
                return status(SERVICE_UNAVAILABLE).entity("Unable to get valid db connection").build();
            }
        } catch (SQLException sqlException) {
            log.error("Healthcheck returned sql exception.", sqlException);
            return status(SERVICE_UNAVAILABLE).entity(sqlException.getMessage()).build();
        }

    }

}
