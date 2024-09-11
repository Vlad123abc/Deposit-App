package deposit.networking.jsonProtocol;

import java.util.Objects;

public class Request {
    private RequestType type;
    private Object data;

    public Request() {}

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return type == request.type && Objects.equals(data, request.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, data);
    }

    @Override
    public String toString() {
        return "Request{" +
                "type=" + type +
                ", data=" + data +
                '}';
    }

    public static class Builder {
        private Request request = new Request();
        public Builder setType(RequestType type) {
            request.setType(type);
            return this;
        }
        public Builder setData(Object data) {
            request.setData(data);
            return this;
        }
        public Request build()
        {
            return request;
        }
    }
}
