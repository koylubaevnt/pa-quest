package com.pa.march.paquestserver.converter;

import com.pa.march.paquestserver.domain.Content;
import com.pa.march.paquestserver.domain.ContentType;
import com.pa.march.paquestserver.message.resource.ContentResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
@Slf4j
public class ContentToResourceConverter extends ConversionServiceAwareConverter<Content, ContentResource> {

    @Override
    public ContentResource convert(Content content) {
        log.debug("content={}", content);
        ContentResource contentResource = new ContentResource();
        contentResource.setId(content.getId());
        contentResource.setType(content.getType());
        contentResource.setExt(content.getExt());
        contentResource.setFileName(content.getFileName());
        if (content.getType().equals(ContentType.FILE)) {
            try {
                contentResource.setContent(content.getContent().getBytes(0, (int) content.getContent().length()));
            } catch (SQLException e) {
                log.error("e={}, e.msg={}, e.stacktrace={}", e, e.getMessage(), e.getStackTrace());
            }
        }

        return contentResource;
    }

}
