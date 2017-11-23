# eTreatMD-Project
This app would have two pages:

- Patients list
- Patients profile page

-------------------------------------

Page1: 

When the app launches, you need to set a HTTP GET request to

http://159.203.62.239:3000/patients.json

The response should contain a list of patients with their names and ID in a simple JSON array. Once you fetched this list, populate the patients name on a listview. It's up to you how you want to layout the page element. In case if something is wrong with the server, host "patients.json" on your own and fetch it from the device. The goal is really to see how you design code that involves API callings.

-------------------------------------

Page2:

Page2 is shown when the device user selects a patient from the page1's list. This page should contain the name and the ID of the patients. Also, it should have a button that turns on the camera to capture a profile picture for the patients. The picture taken should be displayed on this page as well. 
