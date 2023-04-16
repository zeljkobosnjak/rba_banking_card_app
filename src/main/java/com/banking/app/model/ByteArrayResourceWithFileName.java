package com.banking.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.ByteArrayResource;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ByteArrayResourceWithFileName {

    private String fileName;
    private ByteArrayResource byteArrayResource;
}
