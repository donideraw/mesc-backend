package com.pertamina.backend.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pertamina.backend.helper.DataStatus;
import com.pertamina.backend.model.dto.AppAuth;
import com.pertamina.backend.model.dto.AttributeDto;
import com.pertamina.backend.model.entity.BaseData;
import com.pertamina.backend.model.entity.BillMaterial;
import com.pertamina.backend.model.entity.MaterialMaster;
import com.pertamina.backend.model.entity.TypeData;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

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
    var sheet = workbook.getSheetAt(0);
    List<BaseData> baseDataList = new ArrayList<>();

    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
      var data = sheet.getRow(i);
      if (data == null) continue;

      BaseData equipmentData = new BaseData();
      equipmentData.setEquipmentId(getString(data.getCell(1)));
      equipmentData.setCategory(getString(data.getCell(2)));
      equipmentData.setManufacturer(getString(data.getCell(3)));
      equipmentData.setSerialNo(getString(data.getCell(4)));
      equipmentData.setDescription(getString(data.getCell(5)));
      equipmentData.setFunctionalLocation(getString(data.getCell(7)));
      equipmentData.setModel(getString(data.getCell(8)));
      equipmentData.setSize(getString(data.getCell(9)));
      equipmentData.setWeight(getString(data.getCell(10)));
      equipmentData.setUom(getString(data.getCell(11)));
      equipmentData.setTypeId(getString(data.getCell(12)));
      equipmentData.setIdentificationNo(getString(data.getCell(13)));
      equipmentData.setOriginCountry(getString(data.getCell(16)));
      equipmentData.setConstructionYear(getString(data.getCell(17)));
      equipmentData.setConstructionMonth(getString(data.getCell(18)));
      equipmentData.setStatus(DataStatus.DRAFT);

      baseDataList.add(equipmentData);
    }

    return baseDataList;
  }

  public static List<BaseData> importClassification(Workbook workbook) {
    var sheet = workbook.getSheetAt(0);
    List<BaseData> baseDataList = new ArrayList<>();

    Map<String, Map<String, String>> mapJsonFormat = new HashMap<>();
    ObjectMapper objectMapper = new ObjectMapper();
    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
      var data = sheet.getRow(i);
      if (data == null) continue;

      String equipmentId = getString(data.getCell(0));
      String typeId = getString(data.getCell(1));
      String attribute = getString(data.getCell(2));
      String value = getString(data.getCell(3));

      if (i != sheet.getLastRowNum() &&
              equipmentId.equals(getString(sheet.getRow(i+1).getCell(0)))
      ) {
        Map<String, String> currentMapAttribute = mapJsonFormat.get(equipmentId);
        if (currentMapAttribute == null) {
          currentMapAttribute = new HashMap<>();
        }
          String key = generateKey(attribute.replace('_', ' '));
          currentMapAttribute.put(key, value);

          mapJsonFormat.put(equipmentId, currentMapAttribute);
      }  else {
        Map<String, String> currentMapAttribute = mapJsonFormat.get(equipmentId);
        if (currentMapAttribute == null) {
          currentMapAttribute = new HashMap<>();
        }
        String key = generateKey(attribute.replace('_', ' '));
        currentMapAttribute.put(key, value);

        mapJsonFormat.put(typeId, currentMapAttribute);

        BaseData equipmentData = new BaseData();
        equipmentData.setEquipmentId(equipmentId);
        equipmentData.setTypeId(typeId);
        equipmentData.setClassification(objectMapper.valueToTree(mapJsonFormat.get(equipmentId)));
        baseDataList.add(equipmentData);
      }
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

      String catalogProfileId = getString(data.getCell(0));
      String desc = getString(data.getCell(1));
      String attributeName = getString(data.getCell(2));

      String typeId = catalogProfileId;

      if (i != sheet.getLastRowNum() &&
              typeId.equals(getString(sheet.getRow(i+1).getCell(0)))
      ) {
        Map<String, String> currentMapAttribute = mapJsonFormat.get(typeId);
        List<AttributeDto> attributeDtoList = mapAttribute.get(typeId);
        if (currentMapAttribute == null) {
          attributeDtoList = new ArrayList<>();
          currentMapAttribute = new HashMap<>();
        }
        String key = generateKey(attributeName.replace('_', ' '));
        String tagName = attributeName;
        currentMapAttribute.put(key, "");
        attributeDtoList.add(new AttributeDto(key, tagName));

        mapJsonFormat.put(typeId, currentMapAttribute);
        mapAttribute.put(typeId, attributeDtoList);
      } else {
        Map<String, String> currentMapAttribute = mapJsonFormat.get(typeId);
        List<AttributeDto> attributeDtoList = mapAttribute.get(typeId);
        if (currentMapAttribute == null) {
          attributeDtoList = new ArrayList<>();
          currentMapAttribute = new HashMap<>();
        }
        String key = generateKey(attributeName.replace('_', ' '));
        String tagName = attributeName;
        currentMapAttribute.put(key, "");
        attributeDtoList.add(new AttributeDto(key, tagName));

        mapJsonFormat.put(typeId, currentMapAttribute);
        mapAttribute.put(typeId, attributeDtoList);

        TypeData catalogProfile = new TypeData();
        catalogProfile.setTypeId(catalogProfileId);
        catalogProfile.setDescription(desc);
        catalogProfile.setAttributes(objectMapper.valueToTree(mapAttribute.get(typeId)));
        catalogProfile.setJsonFormat(objectMapper.valueToTree(mapJsonFormat.get(typeId)));
        typeDataList.add(catalogProfile);
      }
    }

    return typeDataList;
  }

  public static List<MaterialMaster> importMaterials(Workbook workbook) {
    var sheet = workbook.getSheetAt(0);
    List<MaterialMaster> materials = new ArrayList<>();

    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
      var data = sheet.getRow(i);
      if (data == null) continue;

      MaterialMaster material = new MaterialMaster();

      material.setMaterial(getString(data.getCell(0)));
      material.setClientLevel(getString(data.getCell(1)));
      material.setBlockClientLevel(getString(data.getCell(2)));
      material.setMaterialType(getString(data.getCell(3)));
      material.setMaterialGroup(getString(data.getCell(4)));
      material.setOldMaterialNumber(getString(data.getCell(5)));
      material.setBaseUnit(getString(data.getCell(6)));
      material.setPartNumber(getString(data.getCell(7)));
      material.setDescription(getString(data.getCell(8)));
      material.setPoText(getString(data.getCell(9)));

      materials.add(material);

    }

    return materials;
  }

  public static List<BillMaterial> importBOM(Workbook workbook) {
    var sheet = workbook.getSheetAt(0);
    List<BillMaterial> bomList = new ArrayList<>();

    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
      var data = sheet.getRow(i);
      if (data == null) continue;

      BillMaterial bom = new BillMaterial();
      bom.setEquipmentId(getString(data.getCell(2)));
      bom.setItemNumber(removeDecimal(getString(data.getCell(4))));
      bom.setSortString(getString(data.getCell(5)));
      bom.setTextLine1(getString(data.getCell(6)));
      bom.setTextLine2(getString(data.getCell(7)));
      bom.setQuantity(getString(data.getCell(10)));
      bom.setUom(getString(data.getCell(11)));
      bom.setPoText(getString(data.getCell(15)));

      bomList.add(bom);
    }

    return bomList;
  }

  private static String removeDecimal(String itemNumber) {
    if (itemNumber != null && !"".equals(itemNumber)) {
      String[] splitted = itemNumber.split("\\.");
      return splitted[0];
    }
    return itemNumber;
  }

  private static String generateKey(String input) {
    String[] words = input.toLowerCase().split(" ");
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < words.length; i++) {
      String word = words[i];
      if (i != 0) {
        // Capitalize the first character of each word
        if (!isNumeric(word) && word.length() > 0) {
          String firstChar = String.valueOf(Character.toUpperCase(word.charAt(0)));
          // Append the remaining characters
          word = firstChar + word.substring(1);
        }
      }
      word = word.replaceAll("[^a-zA-Z0-9 ]", "");
      result.append(word);
    }

    return result.toString();
  }
  private static String getString(Cell cell) {
    return cell == null ? "" : String.valueOf(cell);
  }

  public static boolean isNumeric(String strNum) {
    if (strNum == null) {
      return false;
    }
    try {
      double d = Double.parseDouble(strNum);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }
}
