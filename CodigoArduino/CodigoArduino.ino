/*
Descripción de funcionalidad:
Todo el texto enviado por medio del monitor serial se imprimirá
en la pantalla LCD, y el contraste sera controlado por medio del
potenciometro.

  Circuito:
 * LCD GND a tierra
 * LCD VCC pin a 5V
 * LCD V0 al potenciometro
 * LCD RS pin a pin digital 11
 * LCD RW a tierra
 * LCD Enable pin a pin digital 10
 * LCD D0 a tierra
 * LCD D1 a tierra
 * LCD D2 a tierra
 * LCD D3 a tierra
 * LCD D4 a pin digital 9
 * LCD D5 a pin digital 6
 * LCD D6 a pin digital 5
 * LCD D7 a pin digital 3
 * LCD R/W a tierra
 * LCD VSS a tierra
 * Resistencia  de 330 Ohms
 * Potenciometro 10 kOhms
 */

// Incluir la libreria:
#include <LiquidCrystal.h>

// Inicializar la libreria con el numero de los pines
LiquidCrystal lcd(11, 10, 9, 6, 5, 3);

void setup() {
  // Configurar el tipo de LCD.
  lcd.begin(16, 2);
  // Configurar la comunicación serial.
  Serial.begin(9600);
}

void loop() {
  //Verificar si se tiene información pendiente por revisar
  if(Serial.available()){
    delay(100);
    //Limpiar la pantalla LCD
    lcd.clear();
    while(Serial.available()>0){
      //Mostrar en pantalla lo que encuentre en el monitor serial
      lcd.write(Serial.read());
    }
  }
}
