# Aerin_Lab09_Group03 - Currency Exchange Software

## How To Run
There are two steps to take when running the program:
1. To build the program:
```
gradle build
```
2. To run the program:
```
gradle run
```

### How To Use The Program
#### Currency Converter
When you first open the program, you will be greeted by the currency converter page, this allows you to insert an amount, select the currency of the amount and select the currency to wish you convert to.\
To convert, press the 'Convert' button.

#### Most Popular Page
The second page is a 4x4 table of the four most popular currencies. The four most popular is selected by the admin.

#### Print Summary
The third page allows the user to print a summary of two currencies, with a specified start and end date. A pdf document is downloaded containing the two currencies and their minimum, maximum, average, median and standard deviation. As well as every time the rate was updated by an admin.

#### Admin Login/Register
If the user is an admin, they can log in to update exchange rates. In order to log in, the admin needs to input their employee id and their password. If the user is an admin but does not have and account, they can register by putting in their employee id, username and password.

#### Update Currency
Once an admin as logged in, they can update the exchange rate. The admin can select the currency they wish to update, the old rate will be displayed and the new rate and date can be inputted.\
The 'From' currency cannot be altered.\
The exchange rate of that currency will be updated once admin clicks 'Update'

#### Add Currency


## How To Test

