package com.sivalabs.moviebuffs.config;

import com.sivalabs.moviebuffs.utils.Constants;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Profile(Constants.PROFILE_TEST)
@ControllerAdvice
public class ExceptionHandling /*implements ProblemHandling*/ {

}
