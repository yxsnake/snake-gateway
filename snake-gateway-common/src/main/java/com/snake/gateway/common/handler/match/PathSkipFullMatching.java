package com.snake.gateway.common.handler.match;

import com.snake.gateway.common.interfaces.match.IPathSkipMatch;
import org.springframework.beans.factory.annotation.Value;

import java.util.Set;

public class PathSkipFullMatching implements IPathSkipMatch {
    @Value("#{'${gateway.path.skip.fullMatching}'.split(',')}")
    private Set<String> fullMatching;

    @Override
    public boolean match(String uri) {
        return fullMatching.contains(uri);
    }
}