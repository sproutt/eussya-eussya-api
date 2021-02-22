INSERT INTO member (id, email, password, nick_name, member_id, authentication, provider, role) values (99, 'kiki@gmail.com', '$2a$10$u05HYzfziIorHmGQ9psOf.coB/cMl8cjhI0olx8YRVet4o.7khhVC', 'kiki','kiki@gmail.com', 'Y', 'LOCAL', 'USER');

INSERT INTO mission (id, title, contents, writer_id, deadline_time, running_time, status) values ( 50, 'mission1', 'mission1 contents', 99, '2020-07-10T03:00', '01:00:01', 2);
INSERT INTO mission (id, title, contents, writer_id, deadline_time, running_time, status) values ( 51, 'mission2', 'mission2 contents', 99, '2020-07-11T03:00', '01:00:02', 2);
INSERT INTO mission (id, title, contents, writer_id, deadline_time, running_time, status) values ( 52, 'mission3', 'mission3 contents', 99, '2020-07-12T03:00', '01:00:03', 2);
INSERT INTO mission (id, title, contents, writer_id, deadline_time, running_time, status) values ( 53, 'mission4', 'mission4 contents', 99, '2020-07-13T03:00', '01:00:04', 2);
INSERT INTO mission (id, title, contents, writer_id, deadline_time, running_time, status) values ( 54, 'mission5', 'mission5 contents', 99, '2020-07-14T03:00', '01:00:05', 2);
INSERT INTO mission (id, title, contents, writer_id, deadline_time, running_time, status) values ( 55, 'mission6', 'mission6 contents', 99, '2020-07-15T03:00', '01:00:06', 2);
INSERT INTO mission (id, title, contents, writer_id, deadline_time, running_time, status) values ( 56, 'mission7', 'mission7 contents', 99, '2020-07-16T03:00', '01:00:07', 2);
INSERT INTO mission (id, title, contents, writer_id, deadline_time, running_time, status) values ( 57, 'mission8', 'mission8 contents', 99, '2020-07-17T03:00', '01:00:08', 2);
INSERT INTO mission (id, title, contents, writer_id, deadline_time, running_time, status) values ( 58, 'mission9', 'mission9 contents', 99, '2020-07-18T03:00', '01:00:09', 2);
INSERT INTO mission (id, title, contents, writer_id, deadline_time, running_time, status) values ( 59, 'mission10', 'mission10 contents', 99, '2020-07-19T03:00', '01:00:10', 0);
INSERT INTO mission (id, title, contents, writer_id, deadline_time, running_time, status) values ( 60, 'mission11', 'mission11 contents', 99, '2020-07-20T03:00', '01:00:01', 2);
INSERT INTO mission (id, title, contents, writer_id, deadline_time, running_time, status) values ( 61, 'mission12', 'mission12 contents', 99, '2020-07-21T03:00', '01:00:02', 2);
INSERT INTO mission (id, title, contents, writer_id, deadline_time, running_time, status) values ( 62, 'mission13', 'mission13 contents', 99, '2020-07-21T03:00', '01:00:03', 2);
INSERT INTO mission (id, title, contents, writer_id, deadline_time, running_time, status) values ( 63, 'mission14', 'mission14 contents', 99, '2020-07-21T03:00', '01:00:04', 2);
INSERT INTO mission (id, title, contents, writer_id, deadline_time, running_time, status) values ( 64, 'mission15', 'mission15 contents', 99, '2020-07-24T03:00', '01:00:05', 2);
INSERT INTO mission (id, title, contents, writer_id, deadline_time, running_time, status) values ( 65, 'mission16', 'mission16 contents', 99, '2020-07-24T03:00', '01:00:06', 2);
INSERT INTO mission (id, title, contents, writer_id, deadline_time, running_time, status) values ( 66, 'mission17', 'mission17 contents', 99, '2020-07-26T03:00', '01:00:07', 2);
INSERT INTO mission (id, title, contents, writer_id, deadline_time, running_time, status) values ( 67, 'mission18', 'mission18 contents', 99, '2020-07-27T03:00', '01:00:08', 2);
INSERT INTO mission (id, title, contents, writer_id, deadline_time, running_time, status) values ( 68, 'mission19', 'mission19 contents', 99, '2020-07-28T03:00', '01:00:09', 2);
INSERT INTO mission (id, title, contents, writer_id, deadline_time, running_time, status) values ( 69, 'mission20', 'mission20 contents', 99, '2020-07-29T03:00', '01:00:10', 2);

INSERT INTO activity (id, member_id) values( 51, 99)
INSERT INTO activity (id, member_id) values( 52, 99)
INSERT INTO activity (id, member_id) values( 53, 99)
INSERT INTO activity (id, member_id) values( 54, 99)
INSERT INTO activity (id, member_id) values( 55, 99)
INSERT INTO activity (id, member_id) values( 56, 99)
INSERT INTO activity (id, member_id) values( 57, 99)
INSERT INTO activity (id, member_id) values( 58, 99)


INSERT INTO grass(id, member_id, activity_id, generated_time) values(91, 99, 51, '2020-07-11T03:00')
INSERT INTO grass(id, member_id, activity_id, generated_time) values(92, 99, 52, '2020-07-12T03:00')
INSERT INTO grass(id, member_id, activity_id, generated_time) values(93, 99, 53, '2020-07-13T03:00')
INSERT INTO grass(id, member_id, activity_id, generated_time) values(94, 99, 54, '2020-07-14T03:00')
INSERT INTO grass(id, member_id, activity_id, generated_time) values(95, 99, 55, '2020-07-15T03:00')
INSERT INTO grass(id, member_id, activity_id, generated_time) values(96, 99, 56, '2020-07-16T03:00')
INSERT INTO grass(id, member_id, activity_id, generated_time) values(97, 99, 57, '2020-07-17T03:00')