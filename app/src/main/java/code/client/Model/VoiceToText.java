package code.client.Model;

import java.io.IOException;
import java.net.URISyntaxException;

public abstract class VoiceToText {
    private IHttpConnection connection;

    public VoiceToText() {
    }

    public VoiceToText(IHttpConnection connection) {
        this.connection = connection;
    }

    public IHttpConnection getConnection() {
        return connection;
    }

    public void setConnection(IHttpConnection connection) {
        this.connection = connection;
    }

    public abstract String processAudio() throws IOException, URISyntaxException;
}