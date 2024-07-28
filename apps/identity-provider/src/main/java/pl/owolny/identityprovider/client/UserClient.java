package pl.owolny.identityprovider.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "user-client", url = "http://localhost:8080")
public interface UserClient {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    String test();
}