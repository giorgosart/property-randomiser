import java.lang.reflect.Field;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class PropertyRandomiser {

  private Random random = new Random();

  public <T> T createAndFill(Class<T> clazz) throws Exception {
    T instance = clazz.getDeclaredConstructor().newInstance();

    for (Field field : clazz.getDeclaredFields()) {
      field.setAccessible(true);
      Object value = getRandomValueForField(field);
      field.set(instance, value);
    }

    return instance;
  }

  private Object getRandomValueForField(Field field) throws Exception {
    Class<?> type = field.getType();

    if (type.isEnum()) {
      Object[] enumValues = type.getEnumConstants();
      return type.getEnumConstants()[random.nextInt(enumValues.length)];
    } else if (type.equals(Integer.TYPE) || type.equals(Integer.class)) {
      return random.nextInt();
    } else if (type.equals(Boolean.TYPE) || type.equals(Boolean.class)) {
      return random.nextInt() > 0.5;
    } else if (type.equals(Long.TYPE) || type.equals(Long.class)) {
      return random.nextLong();
    } else if (type.equals(Double.TYPE) || type.equals(Double.class)) {
      return random.nextDouble();
    } else if (type.equals(Float.TYPE) || type.equals(Float.class)) {
      return random.nextFloat();
    } else if (type.equals(String.class)) {
      return UUID.randomUUID().toString();
    } else if (type.equals(BigInteger.class)) {
      return BigInteger.valueOf(random.nextInt());
    } else if (type.equals(LocalDate.class)) {
      return LocalDate.now().minusDays(new Random().nextInt(18));
    } else if (type.equals(Date.class)) {
      return Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
    } else if (type.equals(LocalTime.class)) {
      return LocalTime.MIN.plusSeconds(random.nextLong());
    }
    return createAndFill(type);
  }
}
