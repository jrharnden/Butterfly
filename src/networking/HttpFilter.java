package networking;

import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

public interface HttpFilter extends HttpRequestMatcher {
    int getMaxResponseSize();
	HttpResponse filterResponse(HttpRequest request, HttpResponse response);
}