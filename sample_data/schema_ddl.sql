DROP TABLE IF EXISTS public.player;
CREATE TABLE public.player (
   id serial NOT NULL PRIMARY KEY,
   player_name text NOT NULL,
   hp integer NOT NULL,
   x integer NOT NULL,
   y integer NOT NULL
);

DROP TABLE IF EXISTS public.game_state;
CREATE TABLE public.game_state (
    id serial NOT NULL PRIMARY KEY,
    saved_title text NOT NULL,
    current_map int NOT NULL,
    map1 text NOT NULL,
    map2 text NOT NULL,
    map3 text NOT NULL,
    saved_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    player_id integer NOT NULL
);

ALTER TABLE ONLY public.game_state
    ADD CONSTRAINT fk_player_id FOREIGN KEY (player_id) REFERENCES public.player(id) ON DELETE CASCADE;
ALTER TABLE ONLY public.map
    ADD CONSTRAINT fk_player_id FOREIGN KEY (player_id) REFERENCES public.player(id) ON DELETE CASCADE;