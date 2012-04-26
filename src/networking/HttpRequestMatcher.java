package networking;

import org.jboss.netty.handler.codec.http.HttpRequest;

public interface HttpRequestMatcher {
    boolean filterResponses(HttpRequest httpRequest);
}