package networking;

/**
* Interface for accessing response filters.
*/
public interface HttpResponseFilters {
    HttpFilter getFilter(String hostAndPort);
}