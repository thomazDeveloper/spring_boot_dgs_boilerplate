CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS public.users
(
    user_id uuid PRIMARY KEY UNIQUE DEFAULT uuid_generate_v4(),
    username  varchar(255),
    first_name  varchar(255),
    last_name   varchar(255)
    );