create database publications;

use publications;

create table publication 
(title varchar(300) primary key,year int,journal varchar(100),
page varchar(20),volume int,URL varchar(200),
mdate varchar(20),keyword varchar(100));

create table publication_author 
	(
	title varchar(300) not null,
	author varchar(100) not null,
	primary key(title,author),
	foreign key (title) references publication (title)
	);

select title from publication_author where author = 'Nathan Goodman';
select author from publication_author where title = 'NP-complete Problems Simplified on Tree Schemas.';

select publication.title,mdate,keyword,author,page,year,volume,journal,URL from publication_author,publication 
where (publication.title = publication_author.title) and (publication.title='Pattern Matching in Trees and Nets.');

select publication.title,mdate,keyword,author,page,year,volume,journal,URL from publication_author,publication 
where (publication.title = publication_author.title) and (publication_author.author='Nathan Goodman');

select * from publication, publication_author
where (publication.title = publication_author.title) and (publication.title='Pattern Matching in Trees and Nets.');

select title from publication where (title like '%NP-complete Problems Simplified on Tree Schemas.');
select title from publication where (title like '%Problems');

select distinct(title) from publication_author where (publication_author.author='Nathan Goodman' or publication_author.author='Oded Shmueli');


select distinct author from publication_author where title in (select title FROM publication_author where author = "Nathan Goodman") and author <> "Nathan Goodman";

select distinct title from publication_author where title in 
(select title FROM publication_author where author = "Nathan Goodman") and author = "Oded Shmueli";


