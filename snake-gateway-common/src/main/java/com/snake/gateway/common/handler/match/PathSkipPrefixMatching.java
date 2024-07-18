package com.snake.gateway.common.handler.match;

import com.snake.gateway.common.interfaces.match.IPathSkipMatch;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class PathSkipPrefixMatching implements IPathSkipMatch {

    @Value("#{'${gateway.path.skip.prefixMatching}'.split(',')}")
    private List<String> prefixMatching;

    @Override
    public boolean match(String uri) {
        for (String str : prefixMatching) {
            if (uri.startsWith(str)) {
                return true;
            }
        }
        return false;
    }
}
