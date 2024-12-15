package org.project.gateway.pojo;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Test {
    @JsonProperty("name")
    public String name;
    @JsonProperty("age")
    public String age;
}
