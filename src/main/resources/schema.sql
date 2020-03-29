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
INSERT INTO VerblijfsKeuzes (Naam, Omschrijving) VALUES ('Halfpension','3-gangenmenu \'s avonds');

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

INSERT INTO Boekingen (KlantID,VerblijfsKeuzeID,Datum,BedragVoorschot,AantalPersonen,DatumVan,DatumTot, IsBetaald) VALUES (1,1,'2020-03-17','',5,'2020-09-12','2020-09-14');
INSERT INTO Boekingen (KlantID,VerblijfsKeuzeID,Datum,BedragVoorschot,AantalPersonen,DatumVan,DatumTot, IsBetaald) VALUES (2,2,'2020-02-07','',2,'2020-08-13','2020-08-17');
INSERT INTO Boekingen (KlantID,VerblijfsKeuzeID,Datum,BedragVoorschot,AantalPersonen,DatumVan,DatumTot, IsBetaald) VALUES (3,2,'2020-01-28','',1,'2020-07-10','2020-07-11');
INSERT INTO Boekingen (KlantID,VerblijfsKeuzeID,Datum,BedragVoorschot,AantalPersonen,DatumVan,DatumTot, IsBetaald) VALUES (4,2,'2020-03-18','',4,'2020-10-18','2020-10-20');
INSERT INTO Boekingen (KlantID,VerblijfsKeuzeID,Datum,BedragVoorschot,AantalPersonen,DatumVan,DatumTot, IsBetaald) VALUES (5,1,'2020-01-02','',1,'2021-01-05','2021-01-09');
INSERT INTO Boekingen (KlantID,VerblijfsKeuzeID,Datum,BedragVoorschot,AantalPersonen,DatumVan,DatumTot, IsBetaald) VALUES (1,1,'2020-01-06','',5,'2020-11-28','2020-12-02');
INSERT INTO Boekingen (KlantID,VerblijfsKeuzeID,Datum,BedragVoorschot,AantalPersonen,DatumVan,DatumTot, IsBetaald) VALUES (2,2,'2020-02-29','',3,'2020-12-15','2020-12-18');
INSERT INTO Boekingen (KlantID,VerblijfsKeuzeID,Datum,BedragVoorschot,AantalPersonen,DatumVan,DatumTot, IsBetaald) VALUES (3,1,'2020-03-17','',4,'2020-09-01','2020-09-03');

CREATE TABLE KamerTypes
(
KamerTypeID INT PRIMARY KEY AUTO_INCREMENT,
Omschrijving VARCHAR(500)
);

INSERT INTO KamerTypes (Omschrijving) VALUES ('2-persoonskamer met douche');
INSERT INTO KamerTypes (Omschrijving) VALUES ('2-persoonskamer met bad');
INSERT INTO KamerTypes (Omschrijving) VALUES ('1-persoonskamer met douche');
INSERT INTO KamerTypes (Omschrijving) VALUES ('1-persoonskamer met bad');

CREATE TABLE Prijzen 
(
PrijsID INT PRIMARY KEY AUTO_INCREMENT,
KamerTypeID INT,
VerblijfsKeuzeID INT,
PrijsPerPersoon DECIMAL,
DatumVanaf DATE,
FOREIGN KEY (KamerTypeID) REFERENCES KamerTypes(KamerTypeID),
FOREIGN KEY (VerblijfsKeuzeID) REFERENCES VerblijfsKeuzes(VerblijfsKeuzeID)
);

INSERT INTO Prijzen (KamerTypeID,VerblijfsKeuzeID,PrijsPerPersoon,DatumVanaf) VALUES (1,1,'31.50','2020-01-01');
INSERT INTO Prijzen (KamerTypeID,VerblijfsKeuzeID,PrijsPerPersoon,DatumVanaf) VALUES (3,2,'47.00','2020-01-01');

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
FOREIGN KEY BoekingID REFERENCES Boekingen(BoekingID),
FOREIGN KEY KamerID REFERENCES Kamers(KamerID)
);

INSERT INTO BoekingDetails (BoekingID,KamerID) VALUES (1,1);
INSERT INTO BoekingDetails (BoekingID,KamerID) VALUES (2,3);
INSERT INTO BoekingDetails (BoekingID,KamerID) VALUES (3,3);
INSERT INTO BoekingDetails (BoekingID,KamerID) VALUES (4,3);
INSERT INTO BoekingDetails (BoekingID,KamerID) VALUES (5,1);
INSERT INTO BoekingDetails (BoekingID,KamerID) VALUES (6,1);
INSERT INTO BoekingDetails (BoekingID,KamerID) VALUES (7,3);
INSERT INTO BoekingDetails (BoekingID,KamerID) VALUES (8,1);