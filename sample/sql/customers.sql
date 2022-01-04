--
-- Copyright 2020 Simon Schiller
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--      http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

INSERT INTO customers(name, age) VALUES ("Mikael Burke", 38);
INSERT INTO customers(name, age) VALUES ("Ayana Clarke", 12);
INSERT INTO customers(name, age) VALUES ("Malachy Wall", 24);

INSERT INTO creditcards(customerId, cardNumber) VALUES (1, "4098 6178 7375 6825");
INSERT INTO creditcards(customerId, cardNumber) VALUES (3, "4003 8097 1909 0394");
