package land.test.inv.common;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.MinguoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Optional;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;

public class Utils {

    public static <T> T transform(final Object obj, final Class<T> clz) {
        if (clz.isInstance(obj)) {
            return clz.cast(obj);
        }
        return null;
    }

    public static String toString(final Object obj) {

        if (obj != null) {
            if (obj.getClass().isInstance(Integer.class)) {
                return String.valueOf(Optional.ofNullable(transform(obj, Integer.class)).orElse(0));
            }
            if (obj.getClass().isInstance(Double.class)) {
                return String.valueOf(Optional.ofNullable(transform(obj, Double.class)).orElse(0.0));
            }
            return String.valueOf(obj);
        } else {
            return null;
        }

    }

    public static Integer toInt(final Object value) {
        if (value != null && value.getClass().isAssignableFrom(Double.class)) {
            return toInt(Double.valueOf(value.toString()).intValue());
        }
        return toInt(String.valueOf(value));
    }

    public static Integer toInt(final String value) {
        return toInt(value, 0);
    }

    public static Integer toInt(final String value, final int defaultValue) {
        return NumberUtils.toInt(value, defaultValue);
    }

    public static String minguoDateStr(final LocalDate value, final String formatter) {
        DateTimeFormatter minguoFormatter = new DateTimeFormatterBuilder()
                .appendPattern(formatter)
                .toFormatter()
                .withChronology(MinguoChronology.INSTANCE);
        return minguoFormatter.format(value);
    }

    public static LocalDate minguoLocalDate(final String value, final String formatter) {
        if (Optional.ofNullable(value).orElse("").isBlank()) {
            return null;
        }
        DateTimeFormatter minguoFormatter = new DateTimeFormatterBuilder()
                .appendPattern(formatter)
                .toFormatter()
                .withChronology(MinguoChronology.INSTANCE);
        return LocalDate.from(minguoFormatter.parse(value));
    }

    public static Object cellValueToObject(final Cell cell) {
        return cellValueToObject(cell, null);
    }

    public static Object cellValueToObject(final Cell cell, final CellType cellType) {
        Object val;
        CellType type = cellType == null ? cell.getCellType() : cellType;
        switch (type) {
            case STRING:
                val = cell.getStringCellValue();
                break;
            case NUMERIC:
                // 檢查是否為日期格式
                if (DateUtil.isCellDateFormatted(cell)) {
                    ZoneId taipeiZone = ZoneId.of("Asia/Taipei");
                    val = cell.getDateCellValue().toInstant().atZone(taipeiZone).toLocalDateTime();
                } else {
                    val = cell.getNumericCellValue();
                }
                break;
            case BOOLEAN:
                val = cell.getBooleanCellValue();
                break;
            case FORMULA:
                // 評估公式結果
                val = cellValueToObject(cell, cell.getCachedFormulaResultType());
                break;
            case ERROR:
                val = cell.getErrorCellValue();
                break;
            case BLANK:
            default:
                val = "";
        }
        return val;
    }

}
