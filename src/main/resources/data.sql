-- PRODUCT
INSERT INTO product (id, name, price, stock, deleted)
VALUES
    ('1b9a137d-3f96-46c2-b77f-21e755096289', 'Laptop TEST', 15000000, 10, false),
    (RANDOM_UUID(), 'Laptop A', 15000000, 10, false),
    (RANDOM_UUID(), 'Phone B', 8000000, 25, true),
    (RANDOM_UUID(), 'Headphones C', 1200000, 50, false),
    (RANDOM_UUID(), 'Mouse D', 500000, 40, false);

-- DEAL
INSERT INTO deal (id, name, start_date, end_date)
VALUES
    (RANDOM_UUID(), 'Back to School Discount', '2025-08-01', '2025-08-31'),
    (RANDOM_UUID(), 'Buy Laptop Get Mouse', '2025-08-01', '2025-08-20'),
    (RANDOM_UUID(), 'Free Headphones for Big Orders', '2025-08-01', '2025-09-01');
-- DEAL_ACTION
-- INSERT INTO deal_action (id, deal_id, action_type, value, unit, bonus_product_id)
-- VALUES
--     -- Giảm giá 10% cho chương trình Back to School Discount
--     (RANDOM_UUID(),
--      (SELECT id FROM deal WHERE name = 'Back to School Discount'),
--      'PERCENT_DISCOUNT',
--      10,
--      '%',
--      NULL),
--
--     -- Giảm giá cố định 2000000 VND cho Back to School Discount
--     (RANDOM_UUID(),
--      (SELECT id FROM deal WHERE name = 'Back to School Discount'),
--      'FIXED_DISCOUNT',
--      2000000,
--      'VND',
--      NULL),
--
--     -- Tặng chuột khi mua Laptop A (Buy Laptop Get Mouse)
--     (RANDOM_UUID(),
--      (SELECT id FROM deal WHERE name = 'Buy Laptop Get Mouse'),
--      'ADD_ITEM',
--      1,
--      'quantity',
--      (SELECT id FROM product WHERE name = 'Mouse D')),
--
--     -- Tặng tai nghe miễn phí cho đơn hàng lớn (Free Headphones for Big Orders)
--     (RANDOM_UUID(),
--      (SELECT id FROM deal WHERE name = 'Free Headphones for Big Orders'),
--      'GIFT_ITEM',
--      1,
--      'quantity',
--      (SELECT id FROM product WHERE name = 'Headphones C'));


-- ROLE
INSERT INTO role (id, name) VALUES
    (RANDOM_UUID(), 'ADMIN'),
    (RANDOM_UUID(), 'USER');

-- USER
INSERT INTO app_user (id, username, email, password, full_name) VALUES
      (RANDOM_UUID(), 'admin', 'admin@example.com', '$2a$10$KIX/8BZ8F5QJUO2y4nUIieoL1O2z3Zh5nmmhZ3P7muj4G4ht6w5h6', 'System Admin'),
      ('a82654a0-2903-48a4-9865-5e158b38d664', 'user', 'user@example.com', '$2a$10$9kWnLrW2V4E6LMRr0cI1IuFht0YXozc0c1.4r2sVj9McAOVzhR2nW', 'Normal User');

-- USER_ROLE mapping
INSERT INTO user_role (user_id, role_id)
SELECT u.id, r.id
FROM app_user u
         JOIN role r ON r.name = 'ADMIN'
WHERE u.username = 'admin';

INSERT INTO user_role (user_id, role_id)
SELECT u.id, r.id
FROM app_user u
         JOIN role r ON r.name = 'USER'
WHERE u.username = 'user';