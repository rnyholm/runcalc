# Runners Calculator

Branch | Travis opinion
-------|-------
Master | [![Build Status](https://travis-ci.com/rnyholm/runcalc.svg?branch=master)](https://travis-ci.com/rnyholm/runcalc)
Develop | [![Build Status](https://travis-ci.com/rnyholm/runcalc.svg?branch=dev-v1.0)](https://travis-ci.com/rnyholm/runcalc)

## In short
A simple and minimalistic android app made by a runner for runners who often converts speeds, paces, calculates estimates and so on.

## Features
### Convert pace to speed
Conversion between pace and speed, just give the pace in format **minutes:seconds** or **minutes** and the speed will be given to you in
decimal format like **11.6**.

### Convert speed to pace
Conversion between speed and pace, just give the speed in decimal format like **12.6** or just **12** and the pace will be given to you 
in format **minutes:seconds**.

### Calculate pace
Calculate pace from distance and time. Pace is given in format **minutes:seconds** or **minutes** and time in format 
**hours:minutes:seconds** or **minutes:seconds**. From these two values the pace is calculated and given in format **minutes:seconds**

### Calculate time
Calculate time from distance and pace. Distance is given in decimal format like **15.6** or just **15** and pace in format 
**minutes:seconds** or **minutes**. From these two values the time is calculated and given in format **hours:minutes:seconds**.

### Calculate distance
Calculate distance from time and pace. Time is given in format **hours:minutes:seconds** or **minutes:seconds** and pace in format 
**minutes:seconds** or **minutes**. From these two values the distance is calculated and given in decimal format like **21.00**.

### Metric and imperial
Support for both metric and imperial units. Metric is default, but it's possible to change this in the app. Metric units uses format
**min/km** and **km/h** and imperial uses format **min/mi** and **mi/h**. Any existing calculations will be changed upon unit change.

### Sleek UI
The user interface is minimalistic, simple and sleek. This along with custom fonts and fresh color palette makes the app to a joy to use

### Custom soft keyboard
The app has a completely own soft keyboard, this is a big deal as it not just only prevents faulty input but also
makes the input a lot faster and easier.

### One button
One button, that's all it takes to make all calculations and conversions! The app knows when it's time to make some calculations
and what kind of property that's possible to make depending on your input, therefore only one button is needed.
