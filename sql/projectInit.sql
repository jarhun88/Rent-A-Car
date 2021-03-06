drop table Reservation cascade constraints purge
/
drop table Rent cascade constraints purge
/
drop table Vehicle cascade constraints purge
/
drop table VehicleType cascade constraints purge
/
drop table Customer cascade constraints purge
/
drop table Return cascade constraints purge
/
drop table Branch cascade constraints purge
/
create table Reservation(
confNo integer generated always as identity,
vid integer not null,
cellphone char(10) not null,
fromDateTime timestamp not null,
toDateTime timestamp not null,
primary key(confNo))
/
create table Rent(
rid integer generated always as identity,
vid integer not null,
cellphone char(10) not null,
fromDateTime timestamp not null,
toDateTime timestamp not null,
odometer number not null,
cardName varchar(50) not null,
cardNo char(16) not null,
expDate date not null,
confNo integer not null,
primary key (rid))
/
create table Vehicle(
vid integer generated always as identity,
vlicense char(6) not null,
make varchar(30) not null,
model varchar(30) not null,
year varchar(4) not null,
color varchar(20) not null,
odometer number not null,
status varchar(20) not null,
vtname varchar(9) not null,
location varchar(20) not null,
city varchar(20) not null,
primary key(vid))
/
create table Branch(
location varchar(20),
city varchar(20),
primary key (location, city))
/
create table VehicleType(
vtname varchar(9) primary key,
features varchar(100) not null,
wrate float not null,
drate float not null,
hrate float not null,
wirate float not null,
dirate float not null,
hirate float not null,
krate float not null)
/
create table Customer(
cellphone char(10) primary key,
name varchar(50)  not null,
address varchar(50) not null,
dlicense char(9) not null)
/
alter table Vehicle
add foreign key (vtname)
references VehicleType (vtname)
on delete cascade
/
alter table Vehicle
add foreign key (location, city)
references Branch (location, city)
on delete cascade
/
create table Return(
rid integer primary key,
returnDateTime timestamp not null,
odometer number not null,
fulltank char(1) not null,
value number not null)
/
alter table Reservation
add foreign key(vid)
references Vehicle(vid)
on delete cascade
/
alter table Reservation
add foreign key(cellphone)
references Customer(cellphone)
on delete cascade
/
alter table Rent
add foreign key (vid)
references Vehicle (vid)
on delete cascade
/
alter table Rent
add foreign key (cellphone)
references Customer (cellphone)
on delete cascade
/
alter table Rent
add foreign key (confNo)
references Reservation (confNo)
on delete cascade
/
alter table Return
add foreign key (rid)
references Rent (rid)
on delete cascade
/
insert into branch Values (
'UBC', 'Vancouver')
/
insert into branch values (
'Downtown', 'Vancouver')
/
insert into branch values (
'East', 'Richmond')
/
insert into Customer values (
'6041234567', 'John Song', '2366 Main Mall, Vancouver, BC', '789456123')
/
insert into Customer values (
'6041237890', 'Satori Kitamori', '5726 University Blvd, Vancouver, BC', '789456124')
/
insert into Customer values (
'7804564567', 'James Ens', '6133 University Blvd, Vancouver, BC', '789456125')
/
insert into Customer values (
'7804561234', 'Jon Snow', '1 Winterfell Rd, Westeros', '789456126')
/
insert into Customer values (
'6041231234', 'Darth Vader', '4 Tusken blvd, Tatooine', '789456127')
/
insert into Customer values (
'1234567890', 'Frodo Baggins', '8 Hobbit St, Shire', '789456128')
/
insert into Customer values (
'1234564560', 'Freddy Krueger', '5 Elm Street, Spooky Town', '789456129')
/
insert into Customer values (
'1237894567', 'Jessica Wong', '2366 Main Mall, Vancouver, BC', '789456130')
/
insert into Customer values (
'4561237890', 'Raymond Ng', '2366 Main Mall, Vancouver, BC', '789456131')
/
insert into Customer values (
'4564561234', 'Santa Ono', '123 Sesame st, Vancouver, BC', '789456132')
/
insert into VehicleType values (
'Economy', 'great value', 60, 10, 0.5, 10, 5, 1, 0.25)
/
insert into VehicleType values (
'Compact', 'easy to drive', 60, 10, 0.5, 10, 5, 1, 0.25)
/
insert into VehicleType values (
'Mid-size', 'good for vacation', 100, 15, 0.75, 5, 2, 1, 0.5)
/
insert into VehicleType values (
'Standard', 'most popular', 60, 10, 0.5, 10 ,5, 1, 0.25)
/
insert into VehicleType values (
'Full-size', 'good for road trip', 130, 20, 1.5, 20, 10, 5, 0.5)
/
insert into VehicleType values (
'SUV', 'reasonable', 130, 20, 1.5, 20, 10, 5, 0.5)
/
insert into VehicleType values (
'Truck', 'for any purpose', 130, 20, 1.5, 20, 10 , 5, 0.5)
/
insert into vehicle (vlicense, make, model, year, color, odometer, status, vtname, location, city) values (
'123456', 'Ford', 'Fusion', '2019', 'Red', 123456789, 'available',
'Economy', 'UBC', 'Vancouver')
/
insert into vehicle (vlicense, make, model, year, color, odometer, status, vtname, location, city) values (
'123457', 'Ford', 'EcoSport', '2019', 'Black', 200000008, 'available',
'Compact', 'UBC', 'Vancouver')
/
insert into vehicle (vlicense, make, model, year, color, odometer, status, vtname, location, city) values (
'123458', 'Ford', 'Ranger', '2019', 'Green', 321221458, 'available',
'Mid-size', 'UBC', 'Vancouver')
/
insert into vehicle (vlicense, make, model, year, color, odometer, status, vtname, location, city) values (
'123459', 'Ford', 'Fusion', '2018', 'Red Black', 151031724, 'available',
'Standard', 'UBC', 'Vancouver')
/
insert into vehicle (vlicense, make, model, year, color, odometer, status, vtname, location, city) values (
'123460', 'GMC', 'Yukon', '2018', 'Grey', 990128, 'maintenance',
'Full-size', 'Downtown', 'Vancouver')
/
insert into vehicle (vlicense, make, model, year, color, odometer, status, vtname, location, city) values (
'123461', 'GMC', 'Acadia', '2018', 'Grey', 10, 'available',
'SUV', 'Downtown', 'Vancouver')
/
insert into vehicle (vlicense, make, model, year, color, odometer, status, vtname, location, city) values (
'123462', 'Ford', 'F-150', '2018', 'Black', 321221458, 'available',
'Truck', 'East', 'Richmond')
/
insert into vehicle (vlicense, make, model, year, color, odometer, status, vtname, location, city) values (
'123463', 'Ford', 'F-150', '2012', 'Red', 321221458, 'available',
'Truck', 'East', 'Richmond')
/
insert into vehicle (vlicense, make, model, year, color, odometer, status, vtname, location, city) values (
'123464', 'Honda', 'CR-V', '2012', 'Grey', 321221458, 'available',
'SUV', 'East', 'Richmond')
/
insert into vehicle (vlicense, make, model, year, color, odometer, status, vtname, location, city) values (
'123465', 'Honda', 'Accord', '2012', 'Black', 321221458, 'available',
'Economy', 'East', 'Richmond')
/
insert into reservation (vid, cellphone, fromDateTime, toDateTime) values (
1, '6041234567', to_timestamp('2019-01-01:00:00','YYYY-MM-DD:HH24:MI'), to_timestamp('2019-01-07:00:00','YYYY-MM-DD:HH24:MI'))
/
insert into reservation (vid, cellphone, fromDateTime, toDateTime) values (
2, '6041237890', to_timestamp('2019-01-01:00:00','YYYY-MM-DD:HH24:MI'), to_timestamp('2019-01-07:00:00','YYYY-MM-DD:HH24:MI'))
/
insert into reservation (vid, cellphone, fromDateTime, toDateTime) values (
3, '7804564567', to_timestamp('2019-06-01:00:00','YYYY-MM-DD:HH24:MI'), to_timestamp('2019-06-30:00:00','YYYY-MM-DD:HH24:MI'))
/
insert into reservation (vid, cellphone, fromDateTime, toDateTime) values (
4, '7804561234', to_timestamp('2019-02-01:00:00','YYYY-MM-DD:HH24:MI'), to_timestamp('2019-02-02:00:00','YYYY-MM-DD:HH24:MI'))
/
insert into reservation (vid, cellphone, fromDateTime, toDateTime) values (
5, '6041231234', to_timestamp('2019-02-01:00:00','YYYY-MM-DD:HH24:MI'), to_timestamp('2019-02-02:00:00','YYYY-MM-DD:HH24:MI'))
/
insert into reservation (vid, cellphone, fromDateTime, toDateTime) values (
6, '1234567890', to_timestamp('2019-02-01:00:00','YYYY-MM-DD:HH24:MI'), to_timestamp('2019-03-01:00:00','YYYY-MM-DD:HH24:MI'))
/
insert into reservation (vid, cellphone, fromDateTime, toDateTime) values (
7, '1234564560', to_timestamp('2019-02-01:00:00','YYYY-MM-DD:HH24:MI'), to_timestamp('2019-03-01:00:00','YYYY-MM-DD:HH24:MI'))
/
insert into reservation (vid, cellphone, fromDateTime, toDateTime) values (
8, '1237894567', to_timestamp('2019-01-01:00:00','YYYY-MM-DD:HH24:MI'), to_timestamp('2019-01-07:00:00','YYYY-MM-DD:HH24:MI'))
/
insert into reservation (vid, cellphone, fromDateTime, toDateTime) values (
9, '4561237890', to_timestamp('2019-01-01:00:00','YYYY-MM-DD:HH24:MI'), to_timestamp('2019-02-01:00:00','YYYY-MM-DD:HH24:MI'))
/
insert into reservation (vid, cellphone, fromDateTime, toDateTime) values (
10, '4564561234', to_timestamp('2019-01-01:00:00','YYYY-MM-DD:HH24:MI'), to_timestamp('2019-02-01:00:00','YYYY-MM-DD:HH24:MI'))
/
insert into rent (vid, cellphone, fromDateTime, toDateTime, odometer, cardName, cardNo, expDate, confNo) values (
1, '6041234567', to_timestamp('2019-01-01:00:00', 'YYYY-MM-DD:HH24:MI'), to_timestamp('2019-01-07:00:00', 'YYYY-MM-DD:HH24:MI'), 123456789, 'Visa', '4111111111111111', to_date('2020-01-01', 'YYYY-MM-DD'), 1)
/
insert into rent (vid, cellphone, fromDateTime, toDateTime, odometer, cardName, cardNo, expDate, confNo) values (
2, '6041237890', to_timestamp('2019-01-01:00:00', 'YYYY-MM-DD:HH24:MI'), to_timestamp('2019-01-07:00:00', 'YYYY-MM-DD:HH24:MI'), 200000008, 'Visa', '4012888888881881', to_date('2022-01-01', 'YYYY-MM-DD'), 2)
/
insert into rent (vid, cellphone, fromDateTime, toDateTime, odometer, cardName, cardNo, expDate, confNo) values (
8, '1237894567', to_timestamp('2019-01-01:00:00', 'YYYY-MM-DD:HH24:MI'), to_timestamp('2019-01-07:00:00', 'YYYY-MM-DD:HH24:MI'), 321221458, 'Mastercard', '5105105105105300', to_date('2023-07-01', 'YYYY-MM-DD'), 8)
/
insert into rent (vid, cellphone, fromDateTime, toDateTime, odometer, cardName, cardNo, expDate, confNo) values (
9, '4561237890', to_timestamp('2019-01-01:00:00', 'YYYY-MM-DD:HH24:MI'), to_timestamp('2019-02-01:00:00', 'YYYY-MM-DD:HH24:MI'), 321221458, 'Visa', '4012888888881891', to_date('2022-01-01', 'YYYY-MM-DD'), 9)
/
insert into rent (vid, cellphone, fromDateTime, toDateTime, odometer, cardName, cardNo, expDate, confNo) values (
10, '4564561234', to_timestamp('2019-01-01:00:00', 'YYYY-MM-DD:HH24:MI'), to_timestamp('2019-02-01:00:00', 'YYYY-MM-DD:HH24:MI'), 321221458, 'Mastercard', '5105105105105122', to_date('2023-07-01', 'YYYY-MM-DD'), 10)
/
insert into rent (vid, cellphone, fromDateTime, toDateTime, odometer, cardName, cardNo, expDate, confNo) values (
4, '7804561234', to_timestamp('2019-02-01:00:00', 'YYYY-MM-DD:HH24:MI'), to_timestamp('2019-02-02:00:00', 'YYYY-MM-DD:HH24:MI'), 151031724, 'Visa', '4111111111111133', to_date('2022-01-01', 'YYYY-MM-DD'), 4)
/
insert into rent (vid, cellphone, fromDateTime, toDateTime, odometer, cardName, cardNo, expDate, confNo) values (
5, '6041231234', to_timestamp('2019-02-01:00:00', 'YYYY-MM-DD:HH24:MI'), to_timestamp('2019-02-02:00:00', 'YYYY-MM-DD:HH24:MI'), 990128, 'Mastercard', '5555555555554444', to_date('2023-07-01', 'YYYY-MM-DD'), 5)
/
insert into rent (vid, cellphone, fromDateTime, toDateTime, odometer, cardName, cardNo, expDate, confNo) values (
6, '1234567890', to_timestamp('2019-02-01:00:00', 'YYYY-MM-DD:HH24:MI'), to_timestamp('2019-03-01:00:00', 'YYYY-MM-DD:HH24:MI'), 10, 'Mastercard', '5105105105105100', to_date('2022-01-01', 'YYYY-MM-DD'), 6)
/
insert into rent (vid, cellphone, fromDateTime, toDateTime, odometer, cardName, cardNo, expDate, confNo) values (
7, '1234564560', to_timestamp('2019-02-01:00:00', 'YYYY-MM-DD:HH24:MI'), to_timestamp('2019-03-01:00:00', 'YYYY-MM-DD:HH24:MI'), 321221458, 'Mastercard', '5105105105105200', to_date('2023-07-01', 'YYYY-MM-DD'), 7)
/
insert into rent (vid, cellphone, fromDateTime, toDateTime, odometer, cardName, cardNo, expDate, confNo) values (
3, '7804564567', to_timestamp('2019-06-01:00:00', 'YYYY-MM-DD:HH24:MI'), to_timestamp('2019-06-30:00:00', 'YYYY-MM-DD:HH24:MI'), 321221458, 'Visa', '4111111111111122', to_date('2023-07-01', 'YYYY-MM-DD'), 3)
/
insert into Return values (
1, to_timestamp('2019-01-07:00:00','YYYY-MM-DD:HH24:MI'), 123456801, 'T', 111)
/
insert into Return values (
2, to_timestamp('2019-01-07:00:00','YYYY-MM-DD:HH24:MI'), 200000153, 'T', 345)
/
insert into Return values (
3, to_timestamp('2019-01-07:00:00','YYYY-MM-DD:HH24:MI'), 321221464, 'T', 454)
/
insert into Return values (
4, to_timestamp('2019-02-01:00:00','YYYY-MM-DD:HH24:MI'), 321221711, 'F', 444)
/
insert into Return values (
5, to_timestamp('2019-02-01:00:00','YYYY-MM-DD:HH24:MI'), 321221590, 'F', 455)
/
insert into Return values (
6, to_timestamp('2019-02-02:00:00','YYYY-MM-DD:HH24:MI'), 151031744, 'T', 677)
/
insert into Return values (
7, to_timestamp('2019-02-02:00:00','YYYY-MM-DD:HH24:MI'), 990179, 'F', 677)
/
insert into Return values (
8, to_timestamp('2019-03-01:00:00','YYYY-MM-DD:HH24:MI'), 170, 'F', 678)
/
insert into Return values (
9, to_timestamp('2019-03-01:00:00','YYYY-MM-DD:HH24:MI'), 321221550, 'T', 666)
/
insert into Return values (
10, to_timestamp('2019-06-30:00:00','YYYY-MM-DD:HH24:MI'), 321221600, 'T', 777)
/