CREATE TABLE public.location (
    id SERIAL PRIMARY KEY,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(50) NOT NULL,
    country VARCHAR(50) NOT NULL,
    zip_code VARCHAR(5) NOT NULL,
    address VARCHAR(50) NOT null,
    accommodation_id INTEGER NOT NULL
);

CREATE TYPE public.accommodation_category_enum AS ENUM (
    'HOTEL',
    'ALTERNATIVE',
    'HOSTEL',
    'LODGE',
    'RESORT',
    'GUEST_HOUSE'
);

CREATE TYPE public.reputation_badge_enum AS ENUM (
    'GREEN',
    'YELLOW',
    'RED'
);

CREATE TABLE accommodation (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    rating numeric(1) NOT NULL,
    category public.accommodation_category_enum NOT NULL,
    image VARCHAR(100) NOT NULL,
    reputation numeric(4) NOT NULL,
    reputation_badge public.reputation_badge_enum NOT NULL,
    price INTEGER NOT NULL,
    availability INTEGER NOT NULL,
    hotelier_id VARCHAR(4) NOT NULL
);

ALTER TABLE ONLY public.location ADD CONSTRAINT accommodation_key_id_fkey FOREIGN KEY (accommodation_id) REFERENCES public.accommodation(id) ON DELETE CASCADE;

CREATE TABLE hotelier (
    id VARCHAR(4) NOT NULL PRIMARY KEY,
    email VARCHAR(30) NOT NULL
);

ALTER TABLE ONLY public.accommodation ADD CONSTRAINT hotelier_key_id_fkey FOREIGN KEY (hotelier_id) REFERENCES public.hotelier(id) ON DELETE CASCADE;


CREATE TABLE customer (
    id SERIAL PRIMARY KEY,
    email VARCHAR(30) NOT NULL
);

CREATE TABLE booking (
    id SERIAL PRIMARY KEY,
    customer_id INTEGER NOT NULL REFERENCES customer(id),
    accommodation_id INTEGER NOT NULL REFERENCES accommodation(id)
);

insert into hotelier (id, email) values ('CMBR', 'cmbr@cmbr.com');
insert into hotelier (id, email) values ('CRFC', 'crfc@crfc.com');

insert into customer (email) values ('customer@org.com');
insert into customer (email) values ('partner@org.com');