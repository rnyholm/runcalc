# Runners Calculator [![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://raw.githubusercontent.com/rnyholm/runcalc/master/LICENSE)

Branch | Travis opinion
-------|-------
Master | [![Build Status](https://travis-ci.com/rnyholm/runcalc.svg?branch=master)](https://travis-ci.com/rnyholm/runcalc)
Develop | [![Build Status](https://travis-ci.com/rnyholm/runcalc.svg?branch=dev-v1.2)](https://travis-ci.com/rnyholm/runcalc)

## Introduction
Runners Calculator is a minimalistic utility application for Android made by a runner for runners. The purpose of the app is to make a few of the most common runner related conversions and calculations a bit easier. This includes conversions between pace and speed and vice versa as well as different calculations of distance, time and pace. Another important goal of the app is that it should be foolproof and easy to use, so you can focus more on the stuff that matters - running🏃‍♂️

Even though the app is made for runners it can be used by anyone in need of these kind of conversions and calculations.

## Features
### Convert pace to speed and vice versa
Does your friend track their runs in speed and you in pace and now you're wondering what pace 11.46km/h is? No problem! Just as easy as stealing candy from a child you can make these conversions, just type in your pace or speed and the converted value will be presented to you.

### Calculate pace
Want to know what pace you need to keep in order to run a half marathon sub 2h? Give the distance and time and the application will calculate the rest for you.

### Calculate time
How long does it take to run 17km at 5:30min/km pace? The answer is given by the app.

### Calculate distance
How far have you run when you spent 1 hour in the running tracks at a pace of 6:17min/km? This is starting to get old but just give this input to the app and the answer will be served to you.

### Estimate VO2max
In how good shape are you really? Just give the result from your cooper test, in meters, and an estimated VO2max will be calculated and presented to you. See [Cooper test on wikipedia](https://en.wikipedia.org/wiki/Cooper_test) for more information about the cooper test and the calculations being made.

### Heart rate zones
Do you also agree that calculating the different heart rate zones is a boring thing to do? Good news, you don't need to do it manually anymore! The app does this work for you, you only need to provide your maximum heart rate or age along with you resting heart rate and the different heart rate zones will be given to you. It's also possible to toggle beast mode(or just experienced runner) which will use slightly different zone limits for the calculations. If you train according to your heart rate zones this setting will make your training, especially on higher intensity a bit more challenging but the rewards may be greater if you're a bit more experienced runner. 

Why is the resting heart rate needed? Because the heart rate zones are calculated with the Karvonen-method which has the heart rate reserve as parameter and you get that by subtracting your resting heart rate from your maximum heart rate. Note, do not mix up your lowest heart rate(which usually is measured while you sleep) with resting heart rate, which you preferable measure when you have woke up but before you get up in the morning.

One last thing, if you don't know your maximum heart rate and uses your age as input instead, the maximum heart rate will be calculated as 220 - age(fox method), which is a less accurate alternative.

See more information about the [Karvonen method on wikipedia](https://en.wikipedia.org/wiki/Heart_rate#Karvonen_method).

### Sleek user interface
The user interface is designed with a minimalistic, simple and sleek mindset. This is to make the app as simple as possible to use and understand. This along with custom fonts and a fresh color palette makes the app a joy to use.

### Simple input
In order to make the app even easier to use, it has a completely custom made keyboard. Which prevents the user for giving faulty input and makes input a lot faster. The different kinds of input; time, pace, speed and distance, uses the same input format across the app. The different input formats are following; time is given in format **hours:minutes:seconds** or **minutes:seconds**, pace in **minutes:seconds**, speed and distance are both given in decimal format like **12.6**.

Even though it's almost foolproof to give user input it's still possible to give faulty input. The input validation is handled in realtime when the input is made and shows directly to the user if something is wrong by changing color of the input field.

### No buttons
Yes it's true, there are no buttons in this app. This is a decision which makes the app even more minimalistic, sleek and easy to use. The app makes calculations and conversions along with users input. If the input is correct and there are enough input for any calculations to be made the app makes them automagically. Isn't this neat or what😁

## Open source
This application is made as an open source hobby project, just because I'm a developer and runner and makes these kind of calculations quite often. The source code is completely free to check out, use and do whatever you want with according to the MIT license. But I would be extremely happy if you leave a comment, star this project or in any other way show your support.

## At last
Run fast, long and happy🏃‍♂️
