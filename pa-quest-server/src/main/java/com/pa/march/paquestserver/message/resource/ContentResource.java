package com.pa.march.paquestserver.message.resource;

import com.pa.march.paquestserver.domain.ContentType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(includeFieldNames = true, callSuper = true)
public class ContentResource extends BaseResource {

    private byte[] content;

    private ContentType type;

    private String ext;

    private String fileName;

}