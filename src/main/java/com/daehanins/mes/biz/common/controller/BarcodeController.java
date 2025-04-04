package com.daehanins.mes.biz.common.controller;

import com.daehanins.mes.biz.common.vo.Barcode;
import com.daehanins.mes.common.utils.BarcodeUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/barcode")
public class BarcodeController {

    @GetMapping(value = "/upca/{barcode}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> UPCABarcode(@PathVariable("barcode") String barcode) throws Exception {
        return okResponse(BarcodeUtil.generateUPCABarcodeImage(barcode));
    }

    @GetMapping(value = "/ean13/{barcode}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> EAN13Barcode(@PathVariable("barcode") String barcode) throws Exception {
        return okResponse(BarcodeUtil.generateEAN13BarcodeImage(barcode));
    }

    @PostMapping(value = "/code128", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> Code128Barcode(@RequestBody String barcode) throws Exception {
        return okResponse(BarcodeUtil.generateCode128BarcodeImage(barcode));
    }

    @PostMapping(value = "/pdf417", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> PDF417Barcode(@RequestBody String barcode) throws Exception {
        return okResponse(BarcodeUtil.generatePDF417BarcodeImage(barcode));
    }

    @PostMapping(value = "/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> QRCode(@RequestBody String barcode) throws Exception {
        return okResponse(BarcodeUtil.generateQRCodeImage(barcode));
    }

    @GetMapping(value = "/qrcode2/{barcode}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] QR2code(@PathVariable("barcode") String barcode) throws Exception {
        BufferedImage barcodeImage = BarcodeUtil.generateQRCodeImage(barcode);
        return imageToByteArray(barcodeImage);
    }

    @PostMapping(value = "/qrcode3", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] QRCode3(@RequestBody String barcode) throws Exception {
        BufferedImage barcodeImage = BarcodeUtil.generateQRCodeImage(barcode);
        return imageToByteArray(barcodeImage);
    }

    private byte[] imageToByteArray(BufferedImage image) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( image, "png", baos );
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();
        return imageInByte;
    }

    private ResponseEntity<BufferedImage> okResponse(BufferedImage image) {
        return new ResponseEntity<BufferedImage>(image, HttpStatus.OK);
    }
}
