/* Jason Scott*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#define MAXLEN 50   // Number of rows
#define WEEK 604800 // 1 week in seconds.
#define LAT 40.8017899 // Basis for latitude
#define LNG -73.704272 // Basis for longitude

float randomlat();
float randomlng();
int randomdate();

int main(int argc, char **argv)
{
  int ids[MAXLEN];
  float latitudes[MAXLEN];
  float longitudes[MAXLEN];
  int dates[MAXLEN];

  int i;
  for(i = 0; i < MAXLEN; i++)
    {
      ids[i] = i + 1;
      latitudes[i] = randomlat();
      longitudes[i] = randomlng();
      dates[i] = randomdate();
      fprintf(stdout, "%d,%f,%f,%d\n", ids[i], latitudes[i], longitudes[i], dates[i]);
    }
}

float randomlat()
{
  float lat = (float)rand()/(float)(RAND_MAX/1.2);
  lat = lat + LAT;
  return lat;
}

float randomlng()
{
  float lng = (float)rand()/(float)(RAND_MAX/.5);
  lng = lng + LNG;
  return lng;
}

int randomdate()
{
   int date = (int) time(NULL);
   
   int diff = rand() % WEEK;

   date = date - diff;
   
   return date;
}
