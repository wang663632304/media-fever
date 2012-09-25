-- --------------------
-- PERSON
-- --------------------
CREATE TABLE Person (
	id		 		bigint(20) 		NOT NULL auto_increment,
	fullname		VARCHAR(255) 	NOT NULL,
	PRIMARY KEY  	(id)
);

-- --------------------
-- WATCHABLE
-- --------------------
CREATE TABLE Watchable (
	id		 		bigint(20) 		NOT NULL auto_increment,
	dtype			VARCHAR(255) 	NOT NULL,
	externalId		bigint(20)		NOT NULL,
	name			VARCHAR(255)	NOT NULL,
	imageURL		VARCHAR(255) 	NULL,
	overview		TEXT			NULL,
	rating			float(10,2)		NOT NULL,
	ratingCount		bigint(20)		NOT NULL,
	releaseYear		bigint(4) 		NULL,
	lastupdated		bigint(20)		NOT NULL,
	tagline			VARCHAR(2000)	NULL,
	trailerURL		VARCHAR(255)	NULL,
	episodeNumber	bigint(20)		NULL,
	PRIMARY KEY  	(id)
);

-- --------------------
-- WATCHABLE_GENRE
-- --------------------
CREATE TABLE Watchable_Genre (
	watchableId		bigint(20)		NOT NULL,
	genre			VARCHAR(255) 	NOT NULL,
	PRIMARY KEY  	(watchableId, genre),
	FOREIGN KEY 	(watchableId) 	REFERENCES Watchable (id)
);

-- --------------------
-- WATCHABLE_ACTOR
-- --------------------
CREATE TABLE Watchable_Actor (
	watchableId		bigint(20)		NOT NULL,
	actorId			bigint(20) 		NOT NULL,
	PRIMARY KEY  	(watchableId, actorId),
	FOREIGN KEY 	(watchableId) 	REFERENCES Watchable (id),
	FOREIGN KEY 	(actorId) 		REFERENCES Person (id)
);

-- --------------------
-- SEASON
-- --------------------
CREATE TABLE Season (
	id		 		bigint(20) 		NOT NULL auto_increment,
	externalId		bigint(20)		NOT NULL,
	seasonNumber	bigint(20)		NOT NULL,
	PRIMARY KEY  	(id)
);

-- --------------------
-- SERIES_SEASON
-- --------------------
CREATE TABLE Series_Season (
	seriesId		bigint(20) 		NOT NULL,
	seasonId		bigint(20)		NOT NULL,
	PRIMARY KEY  	(seriesId, seasonId),
	FOREIGN KEY 	(seriesId) 		REFERENCES Watchable (id),
	FOREIGN KEY 	(seasonId) 		REFERENCES Season (id)
);

-- --------------------
-- SEASON_EPISODE
-- --------------------
CREATE TABLE Season_Episode (
	seasonId		bigint(20)		NOT NULL,
	episodeId		bigint(20) 		NOT NULL,
	PRIMARY KEY  	(seasonId, episodeId),
	FOREIGN KEY 	(seasonId) 		REFERENCES Season (id),
	FOREIGN KEY 	(episodeId) 	REFERENCES Watchable (id)
);

-- --------------------
-- FILE ENTITY
-- --------------------
CREATE TABLE FileEntity (
	id		 		bigint(20) 		NOT NULL auto_increment,
	name			VARCHAR(255) 	NOT NULL,
	content			BLOB			NULL,
	PRIMARY KEY  	(id)
);

-- --------------------
-- FACEBOOK ACCOUNT
-- --------------------
CREATE TABLE FacebookAccount (
	id				bigint(20)		NOT NULL auto_increment,
	userId			VARCHAR(255)	NOT NULL,
	accessToken		VARCHAR(255)	NOT NULL,
	PRIMARY KEY		(id)
);

-- --------------------
-- USER
-- --------------------
CREATE TABLE User (
	id		 		bigint(20) 			NOT NULL auto_increment,
	email			VARCHAR(255)		NOT NULL,
	userToken		VARCHAR(255) 		NOT NULL,
	hashedPassword	VARCHAR(255) 		NOT NULL,
	firstName		VARCHAR(255) 		NOT NULL,
	lastName		VARCHAR(255)		NOT NULL,
	imageId	 		bigint(20) 			NULL,
	socialAccountId	bigint(20)			NULL,
	publicProfile	BOOLEAN				NOT NULL,
	PRIMARY KEY  	(id),
	FOREIGN KEY 	(imageId) 			REFERENCES FileEntity (id),
	FOREIGN KEY		(socialAccountId)	REFERENCES FacebookAccount (id),
	UNIQUE			(email)
);

-- --------------------
-- FRIENDSHIP
-- --------------------
CREATE TABLE Friendship (
	userId		 		bigint(20) 		NOT NULL,
	friendId			bigint(20)		NOT NULL,
	PRIMARY KEY  		(userId, friendId),
	FOREIGN KEY 		(userId) 		REFERENCES User (id),
	FOREIGN KEY 		(friendId) 		REFERENCES User (id)
);

-- --------------------
-- FRIENDREQUEST
-- --------------------
CREATE TABLE FriendRequest (
	id		 			bigint(20) 		NOT NULL auto_increment,
	userId		 		bigint(20) 		NOT NULL,
	senderId			bigint(20)		NOT NULL,
	PRIMARY KEY  		(id),
	FOREIGN KEY 		(userId) 		REFERENCES User (id),
	FOREIGN KEY 		(senderId) 		REFERENCES User (id)
);

-- --------------------
-- USER_WATCHABLE
-- --------------------
CREATE TABLE UserWatchable (
	id		 			bigint(20) 		NOT NULL auto_increment,
	watched				BOOLEAN			NOT NULL,
	isInWishList		BOOLEAN			NOT NULL,
	userId		 		bigint(20) 		NOT NULL,
	watchableId			bigint(20)		NOT NULL,
	PRIMARY KEY  		(id),
	FOREIGN KEY 		(userId) 		REFERENCES User (id),
	FOREIGN KEY 		(watchableId) 	REFERENCES Watchable (id)
);

-- --------------------
-- MEDIA_SESSION
-- --------------------
CREATE TABLE MediaSession (
	id		 			bigint(20) 		NOT NULL auto_increment,
	date				DATETIME		NOT NULL,
	PRIMARY KEY  		(id)
);

-- --------------------
-- MEDIASESSION_WATCHABLETYPE
-- --------------------
CREATE TABLE MediaSession_WatchableType (
	mediaSessionId		bigint(20)		NOT NULL,
	watchableType		VARCHAR(255) 	NOT NULL,
	PRIMARY KEY  	(mediaSessionId, watchableType),
	FOREIGN KEY 	(mediaSessionId) 	REFERENCES MediaSession (id)
);

-- --------------------
-- MEDIA_SELECTION
-- --------------------
CREATE TABLE MediaSelection (
	id		 			bigint(20) 			NOT NULL auto_increment,
	watchableId			bigint(20)			NOT NULL,
	mediaSessionId		bigint(20)			NOT NULL,
	PRIMARY KEY  		(id),
	FOREIGN KEY 		(watchableId) 		REFERENCES Watchable (id),
	FOREIGN KEY 		(mediaSessionId) REFERENCES MediaSession (id)
);

-- --------------------
-- MEDIA_SESSION_USER
-- --------------------
CREATE TABLE MediaSessionUser (
	id		 				bigint(20) 		NOT NULL auto_increment,
	userId		 			bigint(20) 		NOT NULL,
	pendingThumbsUp			bigint(20)		NOT NULL,
	pendingThumbsDown		bigint(20)		NOT NULL,
	accepted				BOOLEAN			NOT NULL,
	mediaSessionId			bigint(20)		NOT NULL,
	PRIMARY KEY  			(id),
	FOREIGN KEY 			(userId) 		REFERENCES User (id),
	FOREIGN KEY 			(mediaSessionId) REFERENCES MediaSession (id)
);

-- --------------------
-- MEDIA_SELECTION_THUMBSUPSUSERS
-- --------------------
CREATE TABLE MediaSelection_ThumbsUpUsers (
	mediaSelectionId		bigint(20)				NOT NULL,
	mediaSessionUserId		bigint(20)				NOT NULL,
	FOREIGN KEY 			(mediaSelectionId) 		REFERENCES MediaSelection (id),
	FOREIGN KEY 			(mediaSessionUserId)	REFERENCES MediaSessionUser (id)
);

-- --------------------
-- MEDIA_SELECTION_THUMBSDOWNSUSERS
-- --------------------
CREATE TABLE MediaSelection_ThumbsDownUsers (
	mediaSelectionId		bigint(20)				NOT NULL,
	mediaSessionUserId		bigint(20)				NOT NULL,
	FOREIGN KEY 			(mediaSelectionId) 		REFERENCES MediaSelection (id),
	FOREIGN KEY 			(mediaSessionUserId)		REFERENCES MediaSessionUser (id)
);

-- --------------------
-- DEVICE
-- --------------------
CREATE TABLE Device (
	id		 		bigint(20) 		NOT NULL auto_increment,
	userId			bigint(20)		NOT NULL,
	installationId	VARCHAR(255) 	NOT NULL,
	registrationId	VARCHAR(255)	NOT NULL,
	deviceType		VARCHAR(255) 	NOT NULL,
	PRIMARY KEY  	(id),
	FOREIGN KEY 	(userId) 		REFERENCES User (id)
);


