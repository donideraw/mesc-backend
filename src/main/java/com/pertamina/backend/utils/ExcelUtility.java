package com.pertamina.backend.utils;

import com.pertamina.backend.helper.DataStatus;
import com.pertamina.backend.model.dto.AppAuth;
import com.pertamina.backend.model.entity.BaseData;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ExcelUtility {

  private ExcelUtility() {}

  public static final String EXCEL_FORMAT =
    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

  public static boolean hasExcelFormat(MultipartFile file) {
    return file != null && EXCEL_FORMAT.equals(file.getContentType());
  }

  public static List<BaseData> importData(Workbook workbook) {
    var sheet = workbook.getSheetAt(1);
    List<BaseData> baseDataList = new ArrayList<>();

    AppAuth auth = SecurityUtil.getAuth();
    /*
     * Start from index 1, skip the header at index 0.
     * Also using $lte (<=) because getLastRowNum is inclusive.
     */
    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
      var data = sheet.getRow(i);
      if (data == null) continue;

      String dataId = getString(data.getCell(3));
      String description = getString(data.getCell(4));
      String detail = getString(data.getCell(5));
      String assignee = getString(data.getCell(1));
      String issuer = getManufacturer(detail);

      var baseData = new BaseData();
      baseData.setDataId(dataId);
      baseData.setDescription(description);
      baseData.setDetail(detail);
      baseData.setIssuer(issuer);
      baseData.setStatus(DataStatus.UPLOADED);
      baseData.setUploadedBy(auth.getUsername());
      baseData.setUploadedAt(LocalDateTime.now());
      if (assignee != null && !assignee.equals("")) {
       baseData.setAssignee(assignee);
       baseData.setStatus(DataStatus.ASSIGNED);
      }
      baseDataList.add(baseData);
    }

    return baseDataList;
  }

  private static String getString(Cell cell) {
    return cell == null ? "" : String.valueOf(cell);
  }

  private static Date parseDate(Cell cell) {
    try {
      return new SimpleDateFormat("dd/MM/yyyy").parse(cell.toString());
    } catch (Exception e) {
      log.warn("Failed to parse date", e);
      return null;
    }
  }

  private static long parseLong(Cell cell) {
    return cell == null ? 0 : Long.parseLong(cell.getStringCellValue());
  }

  private static BigDecimal getBigDecimal(Cell cell) {
    return cell == null
      ? BigDecimal.ZERO
      : BigDecimal.valueOf(cell.getNumericCellValue());
  }

  private static String getManufacturer(String input) {
    String pattern = "MANUFACTURE\\\\s*:\\\\s*([^\\\\r\\\\n]+)";

    Pattern regex = Pattern.compile(pattern);
    Matcher matcher = regex.matcher(input);

    if (matcher.find()) {
      return matcher.group(1);
    }
    return null;
  }
}
