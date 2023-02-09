![Coverage](.github/badges/jacoco.svg)
# cumulus-client
Client Side libraries to assist with accessing CUMULUS API. 

Yaml file created using https://www.json2yaml.com/

With Java Versions < 8.251, HTTP2 calls will not work via OkHttp. This API does not provide 
any HTTP2 enabling, and assumes the applications using this API will be using Java 8.251+ or injecting 
providers such as bouncy castle to enable HTTP2 calls. 
    