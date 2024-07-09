
package com.scrambleticket.handler.qrcode;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QrCodeRequest {
    private String image;
    private String uuid;
}
