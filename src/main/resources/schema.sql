CREATE TABLE Klanten
(
KlantID INT PRIMARY KEY AUTO_INCREMENT,
Voornaam VARCHAR(100) NOT NULL,
Naam VARCHAR(100) NOT NULL,
Straat VARCHAR(255),
Huisnummer VARCHAR(10),
Postcode VARCHAR(6),
Gemeente VARCHAR(50),
Telefoonnummer VARCHAR(20),
Email VARCHAR(150)
);

INSERT INTO Klanten (Voornaam, Naam, Straat, Huisnummer, Postcode, Gemeente, Telefoonnummer, Email) VALUES ('Tom', 'Vanelven', 'straat', '1', '1111', 'gemeente', '0444/446633','tom@hotmail.com');
INSERT INTO Klanten (Voornaam, Naam, Straat, Huisnummer, Postcode, Gemeente, Telefoonnummer, Email) VALUES ('Sam', 'Ceustermans', 'straat', '2', '2222', 'gemeente', '0555/556633','sam@hotmail.com');
INSERT INTO Klanten (Voornaam, Naam, Straat, Huisnummer, Postcode, Gemeente, Telefoonnummer, Email) VALUES ('Atif', 'Khan', 'straat', '3', '3333', 'gemeente', '0555/666633','atif@hotmail.com');
INSERT INTO Klanten (Voornaam, Naam, Straat, Huisnummer, Postcode, Gemeente, Telefoonnummer, Email) VALUES ('Jef', 'Vanuytsel', 'straat', '4', '4444', 'gemeente', '0666/446633','jef@hotmail.com');
INSERT INTO Klanten (Voornaam, Naam, Straat, Huisnummer, Postcode, Gemeente, Telefoonnummer, Email) VALUES ('Cisse', 'Van Echelpoel', 'straat', '5', '5555', 'gemeente', '0555/446633','cisse@hotmail.com');

CREATE TABLE VerblijfsKeuzes
(
VerblijfsKeuzeID INT PRIMARY KEY AUTO_INCREMENT,
Naam VARCHAR(100),
Omschrijving VARCHAR(500)
);

INSERT INTO VerblijfsKeuzes (Naam, Omschrijving) VALUES ('Overnachting met ontbijt','');
INSERT INTO VerblijfsKeuzes (Naam, Omschrijving) VALUES ('Halfpension','3-gangenmenu ''s avonds');

CREATE TABLE Boekingen
(
BoekingID INT PRIMARY KEY AUTO_INCREMENT,
KlantID INT,
VerblijfsKeuzeID INT,
Datum DATE,
BedragVoorschot DECIMAL,
AantalPersonen INT NOT NULL,
DatumVan DATE NOT NULL,
DatumTot DATE,
IsBetaald BOOLEAN,
FOREIGN KEY (KlantID) REFERENCES Klanten(KlantID),
FOREIGN KEY (VerblijfsKeuzeID) REFERENCES VerblijfsKeuzes(VerblijfsKeuzeID)
);

INSERT INTO Boekingen (KlantID,VerblijfsKeuzeID,Datum,BedragVoorschot,AantalPersonen,DatumVan,DatumTot, IsBetaald) VALUES (1,1,'2020-03-17',65,5,'2020-09-12','2020-09-14',false);
INSERT INTO Boekingen (KlantID,VerblijfsKeuzeID,Datum,BedragVoorschot,AantalPersonen,DatumVan,DatumTot, IsBetaald) VALUES (2,2,'2020-02-07',0,2,'2020-08-13','2020-08-17',false);
INSERT INTO Boekingen (KlantID,VerblijfsKeuzeID,Datum,BedragVoorschot,AantalPersonen,DatumVan,DatumTot, IsBetaald) VALUES (3,2,'2020-01-28',70,1,'2020-07-10','2020-07-11',false);
INSERT INTO Boekingen (KlantID,VerblijfsKeuzeID,Datum,BedragVoorschot,AantalPersonen,DatumVan,DatumTot, IsBetaald) VALUES (4,2,'2020-03-18',58,4,'2020-10-18','2020-10-20',false);
INSERT INTO Boekingen (KlantID,VerblijfsKeuzeID,Datum,BedragVoorschot,AantalPersonen,DatumVan,DatumTot, IsBetaald) VALUES (5,1,'2020-01-02',0,1,'2021-01-05','2021-01-09',false);
INSERT INTO Boekingen (KlantID,VerblijfsKeuzeID,Datum,BedragVoorschot,AantalPersonen,DatumVan,DatumTot, IsBetaald) VALUES (1,1,'2020-01-06',45,5,'2020-11-28','2020-12-02',true);
INSERT INTO Boekingen (KlantID,VerblijfsKeuzeID,Datum,BedragVoorschot,AantalPersonen,DatumVan,DatumTot, IsBetaald) VALUES (2,2,'2020-02-29',45,3,'2020-12-15','2020-12-18',true);
INSERT INTO Boekingen (KlantID,VerblijfsKeuzeID,Datum,BedragVoorschot,AantalPersonen,DatumVan,DatumTot, IsBetaald) VALUES (3,1,'2020-03-17',45,4,'2020-09-01','2020-09-03',true);

INSERT INTO Boekingen (KlantID,VerblijfsKeuzeID,Datum,BedragVoorschot,AantalPersonen,DatumVan,DatumTot, IsBetaald) VALUES (1,1,'2019-03-17',0,5,'2019-09-12','2019-09-14',false);
INSERT INTO Boekingen (KlantID,VerblijfsKeuzeID,Datum,BedragVoorschot,AantalPersonen,DatumVan,DatumTot, IsBetaald) VALUES (2,2,'2019-02-07',0,2,'2019-08-13','2019-08-17',false);
INSERT INTO Boekingen (KlantID,VerblijfsKeuzeID,Datum,BedragVoorschot,AantalPersonen,DatumVan,DatumTot, IsBetaald) VALUES (3,2,'2019-01-28',0,1,'2019-07-10','2019-07-11',false);
INSERT INTO Boekingen (KlantID,VerblijfsKeuzeID,Datum,BedragVoorschot,AantalPersonen,DatumVan,DatumTot, IsBetaald) VALUES (4,2,'2019-03-18',0,4,'2019-10-18','2019-10-20',false);
INSERT INTO Boekingen (KlantID,VerblijfsKeuzeID,Datum,BedragVoorschot,AantalPersonen,DatumVan,DatumTot, IsBetaald) VALUES (5,1,'2019-01-02',0,1,'2019-01-05','2019-01-09',false);
INSERT INTO Boekingen (KlantID,VerblijfsKeuzeID,Datum,BedragVoorschot,AantalPersonen,DatumVan,DatumTot, IsBetaald) VALUES (1,1,'2019-01-06',0,5,'2019-11-28','2019-12-02',false);
INSERT INTO Boekingen (KlantID,VerblijfsKeuzeID,Datum,BedragVoorschot,AantalPersonen,DatumVan,DatumTot, IsBetaald) VALUES (2,2,'2019-02-28',0,3,'2019-12-15','2019-12-18',false);
INSERT INTO Boekingen (KlantID,VerblijfsKeuzeID,Datum,BedragVoorschot,AantalPersonen,DatumVan,DatumTot, IsBetaald) VALUES (3,1,'2019-03-17',0,4,'2019-09-01','2019-09-03',false);


CREATE TABLE KamerTypes
(
KamerTypeID INT PRIMARY KEY AUTO_INCREMENT,
Omschrijving VARCHAR(500)
);

INSERT INTO KamerTypes (Omschrijving) VALUES ('2-persoonskamer met douche');
INSERT INTO KamerTypes (Omschrijving) VALUES ('2-persoonskamer met bad');
INSERT INTO KamerTypes (Omschrijving) VALUES ('1-persoonskamer met douche');
INSERT INTO KamerTypes (Omschrijving) VALUES ('1-persoonskamer met bad');

CREATE TABLE Kamers
(
    KamerID INT PRIMARY KEY AUTO_INCREMENT,
    KamerTypeID INT,
    KamerNummer INT,
    FOREIGN KEY (KamerTypeID) REFERENCES KamerTypes(KamerTypeID)
);

INSERT INTO Kamers (KamerTypeID,KamerNummer) VALUES (1,6);
INSERT INTO Kamers (KamerTypeID,KamerNummer) VALUES (1,8);
INSERT INTO Kamers (KamerTypeID,KamerNummer) VALUES (3,4);
INSERT INTO Kamers (KamerTypeID,KamerNummer) VALUES (2,2);

CREATE TABLE Prijzen
(
PrijsID INT PRIMARY KEY AUTO_INCREMENT,
KamerID INT,
VerblijfsKeuzeID INT,
PrijsPerKamer DECIMAL,
DatumVanaf DATE,
FOREIGN KEY (KamerID) REFERENCES Kamers(KamerID),
FOREIGN KEY (VerblijfsKeuzeID) REFERENCES VerblijfsKeuzes(VerblijfsKeuzeID)
);

INSERT INTO Prijzen (KamerID,VerblijfsKeuzeID,PrijsPerKamer,DatumVanaf) VALUES (1,1,'63.00','2020-01-01');
INSERT INTO Prijzen (KamerID,VerblijfsKeuzeID,PrijsPerKamer,DatumVanaf) VALUES (1,2,'94.00','2020-01-01');
INSERT INTO Prijzen (KamerID,VerblijfsKeuzeID,PrijsPerKamer,DatumVanaf) VALUES (2,1,'70.00','2020-01-01');
INSERT INTO Prijzen (KamerID,VerblijfsKeuzeID,PrijsPerKamer,DatumVanaf) VALUES (2,2,'100.00','2020-01-01');
INSERT INTO Prijzen (KamerID,VerblijfsKeuzeID,PrijsPerKamer,DatumVanaf) VALUES (3,1,'57.00','2020-01-01');
INSERT INTO Prijzen (KamerID,VerblijfsKeuzeID,PrijsPerKamer,DatumVanaf) VALUES (3,2,'84.00','2020-01-01');
INSERT INTO Prijzen (KamerID,VerblijfsKeuzeID,PrijsPerKamer,DatumVanaf) VALUES (4,1,'80.00','2020-01-01');
INSERT INTO Prijzen (KamerID,VerblijfsKeuzeID,PrijsPerKamer,DatumVanaf) VALUES (4,2,'110.00','2020-01-01');

CREATE TABLE KamersOnbeschikbaar
(
KamersOnbeschikbaarID INT PRIMARY KEY AUTO_INCREMENT,
KamerID INT,
DatumVan DATE NOT NULL,
DatumTot DATE,
FOREIGN KEY (KamerID) REFERENCES Kamers(KamerID)
);

INSERT INTO KamersOnbeschikbaar (KamerID,DatumVan,DatumTot) VALUES (2,'2020-01-01','2020-12-31');

CREATE TABLE BoekingDetails
(
BoekingDetailsID INT PRIMARY KEY AUTO_INCREMENT,
BoekingID INT,
KamerID INT,
FOREIGN KEY (BoekingID) REFERENCES Boekingen(BoekingID),
FOREIGN KEY (KamerID) REFERENCES Kamers(KamerID)
);

INSERT INTO BoekingDetails (BoekingID,KamerID) VALUES (1,1);
INSERT INTO BoekingDetails (BoekingID,KamerID) VALUES (2,3);
INSERT INTO BoekingDetails (BoekingID,KamerID) VALUES (3,3);
INSERT INTO BoekingDetails (BoekingID,KamerID) VALUES (4,3);
INSERT INTO BoekingDetails (BoekingID,KamerID) VALUES (5,1);
INSERT INTO BoekingDetails (BoekingID,KamerID) VALUES (6,1);
INSERT INTO BoekingDetails (BoekingID,KamerID) VALUES (7,3);
INSERT INTO BoekingDetails (BoekingID,KamerID) VALUES (8,1);

INSERT INTO BoekingDetails (BoekingID,KamerID) VALUES (9,1);
INSERT INTO BoekingDetails (BoekingID,KamerID) VALUES (10,3);
INSERT INTO BoekingDetails (BoekingID,KamerID) VALUES (11,3);
INSERT INTO BoekingDetails (BoekingID,KamerID) VALUES (12,3);
INSERT INTO BoekingDetails (BoekingID,KamerID) VALUES (13,1);
INSERT INTO BoekingDetails (BoekingID,KamerID) VALUES (14,1);
INSERT INTO BoekingDetails (BoekingID,KamerID) VALUES (15,3);
INSERT INTO BoekingDetails (BoekingID,KamerID) VALUES (16,1);

