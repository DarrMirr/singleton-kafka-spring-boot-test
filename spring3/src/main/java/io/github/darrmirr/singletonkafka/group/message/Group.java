package io.github.darrmirr.singletonkafka.group.message;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Model of kafka message.
 */
@Data
@Accessors(chain = true)
public class Group {
    private Integer id;
    private String name;
    private List<Integer> userIds;
}
