import java.io.BufferedReader;
import java.io.FileReader;

public class Zip {

  Area[] postnr;
  int max = 10000;

  public class Area {

    String code;
    String name;
    Integer pop;

    public Area(String code, String name, Integer pop) {
      this.code = code.trim();
      this.name = name.trim();
      this.pop = pop;
    }
  }

  public Zip(String file) {
    this.postnr = new Area[this.max];
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      String line;
      int i = 0;
      while ((line = br.readLine()) != null && i < this.max) {
        String[] row = line.split(",");
        postnr[i++] =
          new Area(row[0].trim(), row[1].trim(), Integer.valueOf(row[2]));
      }
      this.max = i;
    } catch (Exception e) {
      System.out.println(" file " + file + " not found");
    }
  }

  public boolean lookup(String zipValue) {
    boolean found = false;
    Integer input = Integer.valueOf(zipValue.replace(" ", ""));
    for (int i = 0; i < postnr.length; i++) {
      if (postnr[i] == null) {
        break;
      }
      Integer fileZip = Integer.valueOf(postnr[i].code.replace(" ", "").trim());

      if (fileZip == input) {
        found = true;
      }
    }
    return found;
  }

  public boolean binarySearch(String zipValue) {
    boolean found = false;
    int min = 0;
    int max = this.max - 1;
    Integer input = Integer.valueOf(zipValue.trim());
    while (min < max) {
      int indx = (min + max) / 2;
      if (postnr[indx] == null) {
        break;
      }
      Integer fileZip = Integer.valueOf(
        postnr[indx].code.replace(" ", "").trim()
      );
      if (fileZip == input) {
        found = true;
      } else if (input > fileZip && indx < max) {
        min = indx + 1;
      } else if (input < fileZip && indx > min) {
        max = indx - 1;
      }
    }
    return found;
  }

  public static long benchmark(Zip zipfile, String zipValue) {
    long t0 = System.nanoTime();
    zipfile.binarySearch(zipValue);
    long t1 = System.nanoTime();

    return t1 - t0;
  }

  public static void main(String[] args) {
    String file = "postnummer.csv";
    String zipValue = "98499"; //"984 99” , "111 15"

    Zip table = new Zip(file);

    

    
    long total = 0;
    int trials = 100;

    for (int i = 0; i < trials; i++) {
      long time = benchmark(table, zipValue);
      total += time;
    }
    System.out.println(zipValue + " | " + total / trials);
  }
}
