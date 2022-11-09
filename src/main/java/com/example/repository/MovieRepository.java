package com.example.repository;

import com.example.entity.Movie;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped

public class MovieRepository implements PanacheRepository<Movie> {
  public List<Movie> getListMovieByFilter(String title, int status, Page pageable){
    Map<String,Object> params =new HashMap<>();
    params.put("title", title);
    if (status!=0) params.put("status", status);
    String titleQuery = "title like concat('%',:title,'%')";
    String statusQuery = status !=0 ? " and status = :status" : " and status > 0";
    String query = titleQuery + statusQuery;
    return find(query, Sort.by("title", Sort.Direction.Ascending), params).page(pageable).list();
  }
}
