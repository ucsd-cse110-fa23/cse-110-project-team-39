package code.client.Model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MockHttpConnection implements CustomHttpConnection {
    private int responseCode;
    private InputStream inputStream;
    private OutputStream outputStream;

    public MockHttpConnection(int responseCode, InputStream inputStream, OutputStream outputStream) {
        this.responseCode = responseCode;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @Override
    public int getResponseCode() throws IOException {
        return responseCode;
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public InputStream getErrorStream() {
        return inputStream;
    }

    @Override
    public void setRequestMethod(String method) {
    }

    @Override
    public void setRequestProperty(String key, String value) {

    }

    @Override
    public void setDoOutput(boolean output) {
    }

    @Override
    public void disconnect() {
    }
}