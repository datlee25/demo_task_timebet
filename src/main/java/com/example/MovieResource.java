package com.example;

import com.example.entity.Movie;
import com.example.entity.MovieResponse;
import com.example.repository.MovieRepository;
import io.quarkus.panache.common.Page;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.print.Pageable;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/movie")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieResource {

  @Inject
  MovieRepository movieRepository;

  @GET
  public Response getAll(@QueryParam("page") @DefaultValue("0") int page,
    @QueryParam("limit") @DefaultValue("10") int limit) {
    List<Movie> movieList = movieRepository.findAll().page(Page.of(page, limit)).list();
    Map<Integer, List<MovieResponse>> pairMap = movieList.stream()
      .collect(Collectors.groupingBy(Movie::getStatus, Collectors.mapping(movie -> {
        return new MovieResponse(movie.getTitle(), "Le Thanh Dat");
      }, Collectors.toList())));
    return Response.ok(pairMap).build();
  }

  @GET
  @Path("/search")
  public Response getListMovieByFilter(@QueryParam("title") String title, @QueryParam("status") int status,
    @QueryParam("page") @DefaultValue("0") int page, @QueryParam("limit") @DefaultValue("10") int limit) {
    Page pageable = Page.of(page,limit);
    return Response.ok(movieRepository.getListMovieByFilter(title,status,pageable)).build();
  }

  @GET
  @Path("{id}")
  public Response findById(@PathParam("id") Long id) {
    return movieRepository.findByIdOptional(id).map(movie -> Response.ok(movie).build())
      .orElse(Response.status(Response.Status.NOT_FOUND).build());
  }

  @GET
  @Path("/name")
  public Response findByName(@QueryParam("title") String title) {
    return Response.ok(movieRepository.list("title", title)).build();
  }

  @POST
  @Transactional
  public Response save(@RequestBody Movie movie) {
    System.out.println("Movie: " + movie);
    movieRepository.persist(movie);
    if (movieRepository.isPersistent(movie)) {
      return Response.created(URI.create("/movie/" + movie.getId())).build();
    }
    return Response.status(Response.Status.BAD_REQUEST).build();
  }

  @PUT
  @Path("{id}")
  @Transactional
  public Response update(@RequestBody Movie movie, @PathParam("id") Long id) {
    Movie movieInDb = movieRepository.findByIdOptional(id).orElseThrow(() -> new RuntimeException("Movie not found"));
    movieInDb.setTitle(movie.getTitle());
    movieInDb.setStatus(movie.getStatus());
    movieRepository.persist(movieInDb);
    if (movieRepository.isPersistent(movieInDb)) {
      return Response.ok(movieInDb).build();
    }
    return Response.status(Response.Status.BAD_REQUEST).build();
  }
}
