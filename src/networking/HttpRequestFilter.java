package networking;

import org.jboss.netty.handler.codec.http.HttpRequest;

public interface HttpRequestFilter {
    void filter(HttpRequest httpRequest);
}