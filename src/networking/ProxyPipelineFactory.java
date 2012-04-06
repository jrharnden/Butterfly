package networking;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpRequestEncoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;


public class ProxyPipelineFactory implements ChannelPipelineFactory {

	private final ClientSocketChannelFactory cf;
	
	public ProxyPipelineFactory(ClientSocketChannelFactory cf) {
		this.cf = cf;
	}
	
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline p =  Channels.pipeline();
		p.addLast("decoder", new HttpRequestDecoder());
		p.addLast("encoder", new HttpResponseEncoder());
		p.addLast("handler", new ProxyClientHandler(cf));
		return p;
	}

}
