CREATE TABLE users(
	id	BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name	VARCHAR(100) NOT NULL,
    last_name 	VARCHAR(100) not null,
	email	VARCHAR(150) not null,
    password_hash VARCHAR(255) not null,
    role varchar(20) not null,
    department VARCHAR(100),
    manager_id BIGINT,
    enabled BOOLEAN not null DEFAULT true,
    created_at DATETIME not null,
    updated_at DATETIME not null,
    CONSTRAINT uk_user_email UNIQUE(email),
    CONSTRAINT fk_user_manager FOREIGN KEY (manager_id) REFERENCES users(id)
    

);

CREATE index idx_user_email on users(email);
create index idx_user_role on users(role);

create table policies(
	id	BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) not null,
    applied_to_role VARCHAR(20) not null,
    max_budget decimal(12,2) not null,
    max_travel_class varchar(50),
    hard_block_on_violation boolean not null default false,
    active boolean not null default true,
    created_by_id BIGINT,
    created_at DATETIME not null,
    updated_at DATETIME,
    CONSTRAINT fk_policy_creator FOREIGN KEY (created_by_id) REFERENCES users(id)
    
    
);

CREATE index idx_policy_role on policies(applied_to_role);
create index idx_policy_active on policies(active);

create table travel_requests(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT not null,
    destination VARCHAR(200) not null,
    start_date DATE not null,
    end_date DATE not null,
    purpose varchar(255) not null,
    estimated_cost decimal(12,2) not null,
    status varchar(30) not null default 'DRAFT',
    travel_class varchar(50),
    manager_approval_id BIGINT,
    manager_action_id datetime,
    manager_comments varchar(1000),
    fininancial_approval_id BIGINT,
    financial_action_at datetime,
    financial_comments varchar(1000),
    created_at DATETIME not null,
    updated_at DATETIME,
    subimitted_at DATETIME,
    completed_at DATETIME,
    constraint fk_tr_employee FOREIGN KEY (employee_id) REFERENCES users(id),
    constraint fk_tr_manager FOREIGN KEY (manager_approval_id) REFERENCES users(id),
    constraint fk_tr_financial FOREIGN KEY (fininancial_approval_id) REFERENCES users(id)
);  

create index idx_tr_status on travel_requests(status);
create index idx_tr_employee on travel_requests(employee_id);
create index idx_tr_dates on travel_requests(start_date, end_date);

create table itineraries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    travel_request_id BIGINT not null,
    segment_type varchar(20) not null,
    from_location varchar(200) not null,
    to_location varchar(200) not null,
    start_time datetime not null,
    end_time datetime not null,
    details varchar(500),
    constraint fk_itin_tr FOREIGN KEY (travel_request_id) REFERENCES travel_requests(id) on delete cascade
);

create index idx_itin_request on itineraries(travel_request_id);

create table expenses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    travel_request_id BIGINT not null,
    category varchar(30) not null,
    claimed_amount decimal(12,2) not null,
    approved_amount decimal(12,2),
    description varchar(500) not null,
    expense_date date not null,
    receipt_path varchar(500),
    status varchar(30) not null default 'PENDING',
    aprroved_by_id BIGINT,
    approved_at datetime,
    financial_comments varchar(1000),
    reimbursent varchar(30),
    created_at DATETIME not null,
    updated_at DATETIME,
    constraint fk_exp_request FOREIGN KEY (travel_request_id) REFERENCES travel_requests(id) on delete cascade,
    constraint fk_exp_approver FOREIGN KEY (aprroved_by_id) REFERENCES users(id)
);

create index idx_exp_request on expenses(travel_request_id);
create index idx_exp_status on expenses(status);

create table reimbursements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ecpense_id BIGINT not null,
    amount decimal(12,2) not null,
    status varchar(30) not null default 'PENDING',
    processed_by_id BIGINT,
    processed_at datetime,
    referenced_number varchar(100),
    created_at DATETIME not null,
    updated_at DATETIME,
    constraint uk_reimb_expense UNIQUE(expense_id),
    constraint fk_reimb_expense FOREIGN KEY (ecpense_id) REFERENCES expenses(id) on delete cascade,
    constraint fk_reimb_processor FOREIGN KEY (processed_by_id) REFERENCES users(id)
);

create index idx_reimb_status on reimbursements(status);

create table audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    timestamp DATETIME not null,
    actor_id BIGINT not null,
    actor_name varchar(200) not null,
    actror_role varchar(20) not null,
    action varchar(255) not null,
    entity_type varchar(50) not null,
    entity_id BIGINT not null,
    comments varchar(1000)
);

create index idx_audit_timestamp on audit_logs(timestamp);
create index idx_audit_actor on audit_logs(actor_id);
create index idx_audit_entity on audit_logs(entity_type, entity_id);

create table notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    recipient_id BIGINT not null,
    title varchar(200) not null,
    message varchar(1000) not null,
    link_url varchar(500),
    read_flag boolean not null default false,
    created_at DATETIME not null,
    read_at DATETIME,
     constraint fk_notif_recipient FOREIGN KEY (recipient_id) REFERENCES users(id) on delete cascade
);

create index idx_notif_recipient on notifications(recipient_id, read_flag);