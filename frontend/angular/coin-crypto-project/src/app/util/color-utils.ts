export class ColorUtils {
    static getColor(value: number, median: number): string {
      if (value < median) {
        return 'red';
      } else if (value > median) {
        return '#24a349'; // Darker green color
      } else {
        return 'blue';
      }
    }
  }