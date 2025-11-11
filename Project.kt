data class Project(
    var mainIsland: String,
    var Region: String,
    var Province: String,
    var LegislativeDistrict: String,
    var Municipality: String,
    var DistrictEngineeringOffice: String,
    var ProjectId: String,
    var ProjectName: String,
    var TypeOfWork: String,
    var FundingYear: Int,
    var ContractId: String,
    var ApprovedBudgetForContract: Double,//11
    var ContractCost: Double,//12
    var ActualCompletionDate: String,
    var Contractor: String,
    var ContractorCount: Int,//15
    var StartDate: String,
    var ProjectLatitude: Double,//17
    var ProjectLongitude: Double,//18
    var ProvincialCapital: String,
    var ProvincialCapitalLatitude: Double,//20
    var ProvincialCapitalLongitude: Double, //21
    val CostSavings: Double,
    val completionDelayDays: Long
)