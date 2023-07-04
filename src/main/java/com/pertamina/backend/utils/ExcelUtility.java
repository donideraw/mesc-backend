package com.pertamina.backend.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pertamina.backend.helper.DataStatus;
import com.pertamina.backend.model.dto.AppAuth;
import com.pertamina.backend.model.dto.AttributeDto;
import com.pertamina.backend.model.entity.BaseData;
import com.pertamina.backend.model.entity.TypeData;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
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
      String typeId = getString(data.getCell(6));
      String assignee = getString(data.getCell(1));
      String issuer = getManufacturer(detail);

      var baseData = new BaseData();
      baseData.setDataId(dataId);
      baseData.setDescription(description);
      baseData.setDetail(detail);
      baseData.setIssuer(issuer);
      baseData.setTypeId(typeId);
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

  public static List<TypeData> importTypeData(Workbook workbook) {
    var sheet = workbook.getSheetAt(0);
    List<TypeData> typeDataList = new ArrayList<>();

    ObjectMapper objectMapper = new ObjectMapper();

    Map<String, Map<String, String>> mapJsonFormat = new HashMap<>();
    Map<String, List<AttributeDto>> mapAttribute = new HashMap<>();
    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
      var data = sheet.getRow(i);
      if (data == null) continue;

      String typeName = getString(data.getCell(4)).replace(' ', '-');
      String subTypeName = getString(data.getCell(7)).replace(' ', '-');
      String attributeName = getString(data.getCell(18));

      String typeId = typeName + "," + subTypeName;

      if (i != sheet.getLastRowNum() &&
              typeId.equals(getString(sheet.getRow(i+1).getCell(4)).replace(' ', '-') + "," + getString(sheet.getRow(i+1).getCell(7)).replace(' ', '-'))
      ) {
        Map<String, String> currentMapAttribute = mapJsonFormat.get(typeId);
        List<AttributeDto> attributeDtoList = mapAttribute.get(typeId);
        if (currentMapAttribute == null) {
          attributeDtoList = new ArrayList<>();
          currentMapAttribute = new HashMap<>();
        }
        String key = generateKey(attributeName.replace(',', ' '));
        String tagName = attributeName.replace(' ', '-').replace(',', '-');
        currentMapAttribute.put(key, "");
        attributeDtoList.add(new AttributeDto(key, tagName));

        mapJsonFormat.put(typeId, currentMapAttribute);
        mapAttribute.put(typeId, attributeDtoList);
      } else {
        TypeData typeData = new TypeData();
        typeData.setTypeId(subTypeName == "" ? typeName : typeId);
        typeData.setTypeName(typeName);
        typeData.setSubTypeName(subTypeName);
        typeData.setAttributes(objectMapper.valueToTree(mapAttribute.get(typeId)));
        typeData.setJsonFormat(objectMapper.valueToTree(mapJsonFormat.get(typeId)));
        typeDataList.add(typeData);
      }
    }

    return typeDataList;
  }

  private static String generateKey(String input) {
    String[] words = input.toLowerCase().split(" ");
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < words.length; i++) {
      String word = words[i];
      if (i != 0) {
        // Capitalize the first character of each word
        char firstChar = Character.toUpperCase(word.charAt(0));
        // Append the remaining characters
        word = firstChar + word.substring(1);
      }
      word = word.replaceAll("[^a-zA-Z0-9 ]", "");
      result.append(word);
    }

    return result.toString();
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
