package io.github.darrmirr.singletonkafka.user.message;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Model of kafka message.
 */
@Data
@Accessors(chain = true)
public class User {
    private Integer id;
    private String name;
    private String email;
}
