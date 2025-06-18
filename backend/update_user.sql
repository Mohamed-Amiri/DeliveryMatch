-- Update the existing user to be active
UPDATE users SET active = true, verified = true WHERE email = 'john.doe@example.com'; 