# Forge-Career

## Designs and documentations
* Figma: https://www.figma.com/file/KMvkcsnCF4kEtgKwfOeE1M/Untitled?node-id=0%3A1

## Design notes

* UserData Layout
```bash
.
├── Application/
│   └── UserID001/
│       ├── CompanyID001/
│       │   ├── CompanyName: Amazon
│       │   ├── JobType: Full-Time
│       │   ├── PositionType: SWE
│       |   ├── Status: Applied
│       |   ├── Referer: Nick
│       │   ├── StartDate: Summer23
│       │   ├── AppliedDate: 2022-09-01
│       │   ├── Priority: High
│       │   ├── InterviewDate: Null
│       │   └── InterviewRound: 0
│       └── CompanyID002/
│           ├── CompanyName: Google
│           ├── JobType: Full-Time
│           ├── PositionType: SDE
│           ├── Statue: Applied
│           ├── Referer: Tom
│           ├── StartDate: Fall23
│           ├── AppliedDate: 2022-09-01
│           ├── Priority: High
│           ├── InterviewDate: 2022-10-01
│           └── InterviewRound: 0
└── UserInfo
```

* UserData definitation
```bash
.
├── JobType/
│   ├── FullTime
│   ├── PartTime
│   └── Intern
├── Status/
│   ├── Interested
│   ├── Applied
│   ├── OA
│   ├── Interview
│   ├── Reject
│   └── Offered
└── Priority/
    ├── Ultra
    ├── High
    ├── Medium
    └── Low
```
