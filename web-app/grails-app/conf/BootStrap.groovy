import com.munix.*

class BootStrap {
    //Dummy data
	def createPoIdService
	def migratorService
    def migrator =  new Migrator()
    def init = { servletContext ->

        //Assemble
//        def assemble1 = new Assemble(identifier:"Conveyor", description:"Conveyor")
//        assemble1.save()
//        def assemble2 = new Assemble(identifier:"Group A", description:"Pakyawan Group A")
//        assemble2.save()
//        def assemble3 = new Assemble(identifier:"Group B", description:"Pakyawan Group B")
//        assemble3.save()
//        def assemble4 = new Assemble(identifier:"Group C", description:"Pakyawan Group C")
//        assemble4.save()
//        def assemble5 = new Assemble(identifier:"Group D", description:"Pakyawan Group D")
//        assemble5.save()
//
//        //Invoice Type
//        def invoiceType1 = new InvoiceType(identifier:"Shimano", description:"Shimano")
//        invoiceType1.save()
//        def invoiceType2 = new InvoiceType(identifier:"Sister Company", description:"Sister Company")
//        invoiceType2.save()
//        def invoiceType3 = new InvoiceType(identifier:"Regular", description:"Regular")
//        invoiceType3.save()
//
//        //Bank and Branches
//        def bank1 = new Bank(identifier:"BDO", description:"Banco de Oro")
//        def bank2 = new Bank(identifier:"MB", description:"Metro Bank")
//        def bank3 = new Bank(identifier:"BSP", description:"Banco Sentral ng Pilipinas")
//        def bank4 = new Bank(identifier:"PBCOM", description:"Philippine Bank of Commerce")
//        def bank5 = new Bank(identifier:"CB", description:"China Bank")
//
//        def branch1 = new BankBranch(identifier:"MKT", description:"Makati")
//        bank1.addToBranches(branch1)
//        branch1.save()
//        def branch2 = new BankBranch(identifier:"QC", description:"Quezon City")
//        bank2.addToBranches(branch2)
//        branch2.save()
//        def branch3 = new BankBranch(identifier:"MLA", description:"Manila")
//        bank3.addToBranches(branch3)
//        branch3.save()
//        def branch4 = new BankBranch(identifier:"LP", description:"Las pinas")
//        bank4.addToBranches(branch4)
//        branch4.save()
//        def branch5 = new BankBranch(identifier:"QPO", description:"Quiapo Manila")
//        bank3.addToBranches(branch5)
//        branch3.save()
//
//        bank1.save()
//        bank2.save()
//        bank3.save()
//        bank4.save()
//        bank5.save()
//
//        //Bank Account
//        def account1 = new BankAccount(accountName:"siocowiz", accountNumber:"236597874", bank:bank1)
//        account1.save()
//        def account2 = new BankAccount(accountName:"adenny", accountNumber:"12345687", bank:bank3)
//        account2.save()
//        def account3 = new BankAccount(accountName:"ferdz", accountNumber:"69874589", bank:bank1)
//        account3.save()
//        def account4 = new BankAccount(accountName:"steph", accountNumber:"63214587", bank:bank4)
//        account4.save()
//
//        //Check type
//        def checkType1 = new CheckType(routingNumber:1, description:"Local")
//        checkType1.save()
//        def checkType2 = new CheckType(routingNumber:2, description:"Regional")
//        checkType2.save()
//
//        //Company
//        def company1 = new Company(name:"Pacific Cycle International Corporation",
//            registeredAddress:"#1 Balabac Street Dona Imelda, Quezon City",
//            billingAddress:"#1 Balabac Street Dona Imelda, Quezon City",
//            fax:"026339999", landline:"1908-100-munix", email:"adenny.andrade@zigma-sales.com",
//            website:"http://zigma-sales.com")
//        company1.save()
//
//        //CurrencyType
//        def currency1 = new CurrencyType(identifier:"PHP", description:"Philippine Peso")
//        currency1.save()
//        def currency2 = new CurrencyType(identifier:"USD", description:"United States Dollars")
//        currency2.save()
//        def currency3 = new CurrencyType(identifier:"HKD", description:"Hong Kong Dollars")
//        currency3.save()
//
//        //CheckWarehouse
//        def checkWarehouse1 = new CheckWarehouse(identifier:"CWH", description:"Solo Check")
//        checkWarehouse1.save()
//        def checkWarehouse2 = new CheckWarehouse(identifier:"WHC", description:"Duo Check")
//        checkWarehouse2.save()
//        def checkWarehouse3 = new CheckWarehouse(identifier:"HCW", description:"Trio Check")
//        checkWarehouse3.save()
//        def checkWarehouse4 = new CheckWarehouse(identifier:"WCH", description:"Quartet Check")
//        checkWarehouse4.save()
//        def checkWarehouse5 = new CheckWarehouse(identifier:"HWC", description:"Quintet Check")
//        checkWarehouse5.save()
//
//        //Country
//        def country1 = new Country(description:"Philippines")
//        def country2 = new Country(description:"China")
//
//        //CustomerType
//        def customerType1 = new CustomerType(identifier:"AFF", description:"Affiliate")
//        customerType1.save()
//        def customerType2 = new CustomerType(identifier:"CON", description:"Consignee")
//        customerType2.save()
//        def customerType3 = new CustomerType(identifier:"CUS", description:"Customer")
//        customerType3.save()
//
//        //Packaging
//        def packaging1 = new Packaging(identifier:"BX", description:"Box")
//        packaging1.save()
//        def packaging2 = new Packaging(identifier:"PC", description:"Plastic")
//        packaging2.save()
//        def packaging3 = new Packaging(identifier:"SK", description:"Sack")
//        packaging3.save()
//        def packaging4 = new Packaging(identifier:"CN", description:"Container")
//        packaging4.save()
//        def packaging5 = new Packaging(identifier:"BG", description:"Bag")
//        packaging5.save()
//
//        //PaymentType
//        def payment1 = new PaymentType(identifier:"Csh", description:"Cash")
//        payment1.save()
//        def payment2 = new PaymentType(identifier:"Chk", description:"Check")
//        payment2.save()
//
//        //ProductBrand (shimano,voyager,jsutice league, tweety, spiderman, looney toons)
//        def brand1 = new ProductBrand(identifier:"shi", description:"Shimano")
//        brand1.save()
//        def brand2 = new ProductBrand(identifier:"voy", description:"Voyager")
//        brand2.save()
//        def brand3 = new ProductBrand(identifier:"jl", description:"Justice League")
//        brand3.save()
//        def brand4 = new ProductBrand(identifier:"spm", description:"Spiderman")
//        brand4.save()
//        def brand5 = new ProductBrand(identifier:"lt", description:"Looney toons")
//        brand5.save()
//
//        //Category  (rims, battalya, pledals, chair, pedalplate, saddle)
//        def category1 = new ProductCategory(identifier:"rm", description:"Rim")
//        category1.save()
//        def category2 = new ProductCategory(identifier:"batt", description:"Battalya")
//        category2.save()
//        def category3 = new ProductCategory(identifier:"ped", description:"Pedal")
//        category3.save()
//        def category4 = new ProductCategory(identifier:"sad", description:"Saddle")
//        category4.save()
//        def category5 = new ProductCategory(identifier:"chr", description:"Chair")
//        category5.save()
//
//        //Color
//        def color1 = new ProductColor(identifier:"red", description:"Red")
//        color1.save()
//        def color2 = new ProductColor(identifier:"blu", description:"Blue")
//        color2.save()
//        def color3 = new ProductColor(identifier:"ylw", description:"Yellow")
//        color3.save()
//        def color4 = new ProductColor(identifier:"blk", description:"Black")
//        color4.save()
//        def color5 = new ProductColor(identifier:"wht", description:"White")
//        color5.save()
//        def color6 = new ProductColor(identifier:"gld", description:"Gold")
//        color6.save()
//        def color7 = new ProductColor(identifier:"vio", description:"Violet")
//        color7.save()
//
//        //Material (steel, plastic, stainless)
//        def material1 = new ProductMaterial(identifier:"stl", description:"Steel")
//        material1.save()
//        def material2 = new ProductMaterial(identifier:"plstc", description:"Plastic")
//        material2.save()
//        def material3 = new ProductMaterial(identifier:"stnls", description:"Stainless")
//        material3.save()
//
//        //Model
//        def model1 = new ProductModel(identifier:"N95", description:"Nokia N95")
//        model1.save()
//        def model2 = new ProductModel(identifier:"N6500s", description:"Nokia 6500 slide")
//        model2.save()
//        def model3 = new ProductModel(identifier:"B7320", description:"Samsung Omnia Pro")
//        model3.save()
//        def model4 = new ProductModel(identifier:"i8910", description:"Samsung Omnia HD")
//        model4.save()
//        def model5 = new ProductModel(identifier:"V9", description:"Motorola V9")
//        model5.save()
//
//
//        //Region
//        def region1 = new Region(description:"NCR")
//        country1.addToRegions(region1)
//        region1.save()
//
//        def region2 = new Region(description:"Region IV-A")
//        country1.addToRegions(region2)
//        region2.save()
//
//        def region3 = new Region(description:"Beijing")
//        country2.addToRegions(region3)
//        region3.save()
//
//        def region4 = new Region(description:"Xiamen")
//        country2.addToRegions(region4)
//        region4.save()
//
//        country1.save()
//        country2.save()
//
//        //Province
//        def province1 = new Province(description:"Manila")
//        region1.addToProvinces(province1)
//        province1.save()
//        def province2 = new Province(description:"Cavite")
//        region2.addToProvinces(province2)
//        province2.save()
//        def province3 = new Province(description:"Laguna")
//        region2.addToProvinces(province3)
//        province3.save()
//        def province4 = new Province(description:"Batangas")
//        region2.addToProvinces(province4)
//        province4.save()
//        def province5 = new Province(description:"Quezon")
//        region2.addToProvinces(province5)
//        province5.save()
//
//        //City
//        def city1 = new City(description:"Quezon City")
//        province1.addToCities(city1)
//        city1.save()
//        def city2 = new City(description:"Tagaytay")
//        province2.addToCities(city2)
//        city2.save()
//        def city3 = new City(description:"Batangas City")
//        province4.addToCities(city3)
//        city3.save()
//        def city4 = new City(description:"Pasay City")
//        province1.addToCities(city4)
//        city4.save()
//        def city5 = new City(description:"Alfonso")
//        province2.addToCities(city5)
//        city5.save()
//        def city6 = new City(description:"Calamba")
//        province3.addToCities(city6)
//        city6.save()
//
//        //ProductSubcategory
//        def productSubcategory1 = new ProductSubcategory(identifier:"STL", description:"stainless")
//        productSubcategory1.save()
//        def productSubcategory2 = new ProductSubcategory(identifier:"SKL", description:"skinless")
//        productSubcategory2.save()
//        def productSubcategory3 = new ProductSubcategory(identifier:"SPOT", description:"spotted")
//        productSubcategory3.save()
//        def productSubcategory4 = new ProductSubcategory(identifier:"SOL", description:"solid")
//        productSubcategory4.save()
//        def productSubcategory5 = new ProductSubcategory(identifier:"STR",description:"stripes")
//        productSubcategory5.save()
//
//        //Forwarder
//        def forwarder1 = new Forwarder(identifier:"DHL", description:"Dalsey, Hillblom and Lynn", city:city1,
//            zip:"1109", landline:"7218925")
//        forwarder1.save()
//        def forwarder2 = new Forwarder(identifier:"UPS", description:"United Parcel Service", city:city1,
//            zip:"2636", landline:"9837372")
//        forwarder2.save()
//        def forwarder3 = new Forwarder(identifier:"LBC", description:"Luzon Brokerage Corporation",city:city2,
//            zip:"2636", landline:"9837372")
//        forwarder3.save()
//        def forwarder4 = new Forwarder(identifier:"AIR21", description:"Air21", city:city3,
//            zip:"3563", landline:"2626622")
//        forwarder4.save()
//        def forwarder5 = new Forwarder(identifier:"Fedex", description:"Federal Express", city:city3,
//            zip:"9425", landline:"3727222")
//        forwarder5.save()
//        def forwarder6 = new Forwarder(identifier:"USPS", description:"United States Postal Service", city:city4,
//            zip:"7368", landline:"16262727")
//        forwarder6.save()
//
//        //DiscountType
//        def discountType1 = new DiscountType(identifier:"SHI", description:"Shimano")
//        discountType1.save()
//        def discountType2= new DiscountType(identifier:"PAR", description:"Parts")
//        discountType2.save()
//        def discountType3 = new DiscountType(identifier:"PRO", description:"Products")
//        discountType3.save()
//
//        //ProductUnit
//        def productUnit1 = new ProductUnit(identifier:"PCS", description:"pieces")
//        productUnit1.save()
//        def productUnit2= new ProductUnit(identifier:"KG", description:"kilograms")
//        productUnit2.save()
//        def productUnit3 = new ProductUnit(identifier:"UNT", description:"units")
//        productUnit3.save()
//        def productUnit4 = new ProductUnit(identifier:"SAC", description:"sacks")
//        productUnit4.save()
//        def productUnit5 = new ProductUnit(identifier:"BUN", description:"bundles")
//        productUnit5.save()
//
//        //SalesAgent
//        def salesAgent1 = new SalesAgent(identifier:"LS", lastName:"Sioco", firstName:"Louie", street:"Dian Lang",
//            city:city1, zip:"1000", mobile:"11464461", landline:"545645", skype:"SIOCOWIZ",
//            yahoo:"siocowiz@zigma-sales.com")
//        salesAgent1.save()
//        def salesAgent2 = new SalesAgent(identifier:"FC", lastName:"Co", firstName:"Ferdinand", street:"Dito Lang",
//            city:city2, zip:"1109", mobile:"3472624", landline:"246246", skype:"fco",
//            yahoo:"f.co@zigma-sales.com")
//        salesAgent2.save()
//        def salesAgent3 = new SalesAgent(identifier:"AA", lastName:"Andrade", firstName:"Adenny", street:"Dian Lang Din",
//            city:city3, zip:"4123", mobile:"2462462", landline:"4569594", skype:"adenny.andrade",
//            yahoo:"adenny.andrade@yahoo.com")
//        salesAgent3.save()
//
//        //Terms
//        def term1 = new Term(identifier:"COD", description:"COD")
//        term1.save()
//        def term2 = new Term(identifier:"15Days", description:"15 Days")
//        term2.save()
//        def term3 = new Term(identifier:"30Days", description:"30 Days")
//        term3.save()
//        def term4 = new Term(identifier:"45Days", description:"45 Days")
//        term4.save()
//        def term5 = new Term(identifier:"60Days", description:"60 Days")
//        term5.save()
//        def term6 = new Term(identifier:"75Days", description:"75 Days")
//        term6.save()
//        def term7 = new Term(identifier:"90Days", description:"90 Days")
//        term7.save()
//        def term8 = new Term(identifier:"120Days", description:"120 Days")
//        term8.save()
//        def term9 = new Term(identifier:"150Days", description:"150 Days")
//        term9.save()
//        def term10 = new Term(identifier:"180", description:"180 Days")
//        term10.save()
//
//        //Product
//        def product1 = new Product(identifier:"RIMSTAIN110RED",
//            description:"rim 10 stainless n95 steel",
//            brand:brand1, category:category1, color:color1, material:material1,
//            model:model1, subcategory:productSubcategory1, type:discountType1,
//            unit:productUnit1, modelNumber:"1234", partNumber:"123456")
//        product1.save()
//        def product2 = new Product(identifier:"BATSTAIN118YLW",
//            description:"battalya 18 stainless 6500 plastic",
//            brand:brand2, category:category2, color:color3, material:material2,
//            model:model2, subcategory:productSubcategory1, type:discountType3,
//            unit:productUnit3, modelNumber:"2332", partNumber:"6541")
//        product2.save()
//        def product3 = new Product(identifier:"RIMSPOT12BLU",
//            description:"Rim 12 spotted v9 steel shimano",
//            brand:brand1, category:category1, color:color2, material:material1,
//            model:model5, subcategory:productSubcategory3, type:discountType1,
//            unit:productUnit1, modelNumber:"33628", partNumber:"1345")
//        product3.save()
//        def product4 = new Product(identifier:"PEDSTAIN18YLW",
//            description:"Pedal 18 stainless N95 stainless ",
//            brand:brand1, category:category3, color:color3, material:material3,
//            model:model1, subcategory:productSubcategory1, type:discountType2,
//            unit:productUnit1, modelNumber:"78421", partNumber:"9876")
//        product4.save()
//        def product5 = new Product(identifier:"SADSPOT10BLK",
//            description:"Saddle 10 spotted Omnia Plastic",
//            brand:brand1, category:category4, color:color4, material:material2,
//            model:model4, subcategory:productSubcategory3, type:discountType2,
//            unit:productUnit5, modelNumber:"95623", partNumber:"56778")
//        product5.save()
//        def product6 = new Product(identifier:"BATSPOT20GLD",
//            description:"battalya 12 spotted Omnia Plastic ",
//            brand:brand4, category:category4, color:color6, material:material1,
//            model:model2, subcategory:productSubcategory1, type:discountType3,
//            unit:productUnit3, modelNumber:"022354", partNumber:"5422")
//        product6.save()
//        def product7 = new Product(identifier:"SADSTAIN16VIO",
//            description:"Saddle 16 stained Omnia Plastic ",
//            brand:brand3, category:category3, color:color7, material:material3,
//            model:model3, subcategory:productSubcategory2, type:discountType1,
//            unit:productUnit2, modelNumber:"651524", partNumber:"97654")
//        product7.save()
//        def product8 = new Product(identifier:"PEDSPOT12GLD" ,
//            description:"Saddle 10 stained Omnia Plastic",
//            brand:brand3, category:category3, color:color6, material:material2,
//            model:model4, subcategory:productSubcategory2, type:discountType2,
//            unit:productUnit1, modelNumber:"52153", partNumber:"97654")
//        product8.save()
//        def product9 = new Product(identifier:"RIMSPOT16BLK",
//            description:"rim 16 stained Omnia Plastic ",
//            brand:brand1, category:category2, color:color1, material:material3,
//            model:model1, subcategory:productSubcategory2, type:discountType3,
//            unit:productUnit1, modelNumber:"789456", partNumber:"867295")
//        product9.save()
//        def product10 = new Product(identifier:"BATSTAIN10WHT",
//            description:"Saddle 10 stained Omnia Plastic ",
//            brand:brand1, category:category3, color:color5, material:material2,
//            model:model3, subcategory:productSubcategory2, type:discountType2,
//            unit:productUnit2, modelNumber:"1551", partNumber:"97654")
//        product10.save()
//
//        //ProductComponent
//        def productComponent1 = new ProductComponent(component:product1, qty:10, product:product2)
//        productComponent1.save()
//        def productComponent2 = new ProductComponent(component:product1, qty:20, product:product3)
//        productComponent2.save()
//        def productComponent3 = new ProductComponent(component:product1, qty:361, product:product4)
//        productComponent3.save()
//        def productComponent4 = new ProductComponent(component:product1, qty:90, product:product5)
//        productComponent4.save()
//        def productComponent5 = new ProductComponent(component:product2, qty:124522, product:product1)
//        productComponent5.save()
//        def productComponent6 = new ProductComponent(component:product2, qty:105, product:product3)
//        productComponent6.save()
//        def productComponent7 = new ProductComponent(component:product2, qty:1, product:product4)
//        productComponent7.save()
//        def productComponent8 = new ProductComponent(component:product2, qty:6532, product:product5)
//        productComponent8.save()
//
//        //Supplier
//        def supplier1 = new Supplier(identifier:"BC", name:"Biker's Choice", city:city1,
//            term:term1, email:"bc@yahoo.com", contact:"09476027", landline:"2935701",
//            fax:"9371720", skype:"myskype", yahoo:"bcyahoo", website:"bikers.choice.com", tin:"27247258",currency:currency1)
//        supplier1.save()
//        def supplier2 = new Supplier(identifier:"PC", name:"Pacific Cycle", city:city1,
//            term:term4, email:"pc@yahoo.com", contact:"4672", landline:"2935701",
//            fax:"9371720", skype:"myskype", yahoo:"bcyahoo", website:"bikers.choice.com", tin:"27247258",currency:currency1)
//        supplier2.save()
//        def supplier3 = new Supplier(identifier:"CW", name:"Central World", city:city4,
//            term:term6, email:"world@centralworld.com", contact:"2462622", landline:"8346733",
//            fax:"36377272", skype:"yourskype", yahoo:"cwyahoo", website:"central.world.com", tin:"95859492",currency:currency2)
//        supplier3.save()
//        def supplier4 = new Supplier(identifier:"FJ", name:"Fujita", city:city5,
//            term:term5, email:"fujita@yahoo.com", contact:"4672", landline:"2935701",
//            fax:"9371720", skype:"theskype", yahoo:"fujitayahoo", website:"fujita.com", tin:"727255544",currency:currency2)
//        supplier4.save()
//        def supplier5 = new Supplier(identifier:"TS", name:"Triple S", city:city4,
//            term:term9, email:"ts@yahoo.com", contact:"24725373", landline:"947362",
//            fax:"2562267", skype:"ourskype", yahoo:"fujitayahoo", website:"triples.com", tin:"04736222",currency:currency3)
//        supplier5.save()
//
//        //Warehouse (damage, bulacan warehouse, balabac, araneta, white house)
//        def warehouse1 = new Warehouse(identifier:"Dmg", description:"Damage")
//        warehouse1.save()
//        def warehouse2 = new Warehouse(identifier:"Blcn", description:"Bulacan Warehouse")
//        warehouse2.save()
//        def warehouse3 = new Warehouse(identifier:"Blcw", description:"Balabac Warehouse")
//        warehouse3.save()
//        def warehouse4 = new Warehouse(identifier:"Aw", description:"Araneta Warehouse")
//        warehouse4.save()
//        def warehouse5 = new Warehouse(identifier:"Whw", description:"White House Warehouse")
//        warehouse5.save()
//
//        //Discount Group
//        def discountGroup1 = new DiscountGroup(identifier:"DG1", description:"Group 1",rate:20)
//        discountGroup1.save()
//        def discountGroup2 = new DiscountGroup(identifier:"DG2", description:"Group 2",rate:19)
//        discountGroup2.save()
//        def discountGroup3 = new DiscountGroup(identifier:"DG3", description:"Group 3",rate:10)
//        discountGroup3.save()
//
//        //Customer
//        def customer1 = new Customer(identifier:"Jollibee", name:"Louie Sioco",
//            busAddrStreet:"balabac st.", busAddrCity:city1, busAddrZip:"1010",
//            bilAddrStreet:"araneta st", bilAddrCity:city2, bilAddrZip:"1010",
//            skype:"siocowiz", email:"siocowiz@zigma-sales.com", website:"http://sioco.com",
//            landline:"1234567", fax:"7654123", contactPerson:"louie", contactNumber:"0987655432",
//            declaredValue: 100, terms:term1, status:"Complete", forwarder:"",
//            salesAgent: salesAgent1, type:customerType1, discountGroup:discountGroup1, autoApprove:true)
//        customer1.save()
//        def customer2 = new Customer(identifier:"Coca-cola", name:"Adenny Andrade",
//            busAddrStreet:"cubao", busAddrCity:city3, busAddrZip:"1021",
//            bilAddrStreet:"cubao", bilAddrCity:city1, bilAddrZip:"1010",
//            skype:"adenny", email:"adenny.andrade@zigma-sales.com", website:"http://sioco.com",
//            landline:"134654", fax:"7654123", contactPerson:"xa", contactNumber:"09873234",
//            declaredValue: 20, terms:term1, status:"Incomplete", forwarder:"",
//            salesAgent: salesAgent2, type:customerType1, discountGroup:discountGroup1, autoApprove:true)
//        customer2.save()
//        def customer3 = new Customer(identifier:"Hewlett Packard", name:"juan dela cruz",
//            busAddrStreet:"balintawak st", busAddrCity:city1, busAddrZip:"1009",
//            bilAddrStreet:"del monte st", bilAddrCity:city3, bilAddrZip:"1009",
//            skype:"juan", email:"juan.delacruz@yahoo.com", website:"http://sioco.com",
//            landline:"67587987", fax:"7654123", contactPerson:"xadin", contactNumber:"09986384",
//            declaredValue: 30, terms:term2, status:"in Transit", forwarder:"",
//            salesAgent: salesAgent2, type:customerType2, discountGroup:discountGroup2, autoApprove:true)
//        customer3.save()
//        def customer4 = new Customer(identifier:"Google", name:"Nicolo Nicolas",
//            busAddrStreet:"san yun st.", busAddrCity:city1, busAddrZip:"1111",
//            bilAddrStreet:"dito yun st.", bilAddrCity:city3, bilAddrZip:"1111",
//            skype:"nicolo", email:"nicolo@hotmail.com", website:"http://sioco.com",
//            landline:"11122443", fax:"1135363", contactPerson:"dino", contactNumber:"246222",
//            declaredValue: 45, terms:term5, status:"Complete", forwarder:"",
//            salesAgent: salesAgent2, type:customerType2, discountGroup:discountGroup2, autoApprove:true)
//        customer4.save()
//        def customer5 = new Customer(identifier:"Belo Medical Group", name:"Putcholo Prutascio",
//            busAddrStreet:"dinamayan st.", busAddrCity:city1, busAddrZip:"1000",
//            bilAddrStreet:"dito yun st.", bilAddrCity:city2, bilAddrZip:"1000",
//            skype:"putcholo", email:"prutascio@hotmail.com", website:"http://sioco.com",
//            landline:"7654378", fax:"9876789", contactPerson:"potpot", contactNumber:"098900765787",
//            declaredValue: 40, terms:term2, status:"Complete", forwarder:"",
//            salesAgent: salesAgent1, type:customerType3, discountGroup:discountGroup3,autoApprove:true)
//        customer5.save()
//        def customer6 = new Customer(identifier:"Gateway", name:"Kent Hardner",
//            busAddrStreet:"143 Wall Street", busAddrCity:city3, busAddrZip:"2525",
//            bilAddrStreet:"Acapulco Street", bilAddrCity:city4, bilAddrZip:"1010",
//            skype:"kent", email:"siocowiz@zigma-sales.com", website:"http://www.kent.hardner.com",
//            landline:"1234567", fax:"7654123", contactPerson:"Kent", contactNumber:"0987655432",
//            declaredValue: 90, terms:term5, status:"Complete", forwarder:"",
//            salesAgent: salesAgent3, type:customerType2, discountGroup:discountGroup1, autoApprove:false)
//        customer6.save()
//
//        //Truck
//        def truck1 = new Truck(identifier:"truck1", model:"L300", plateNumber:"ZEX248")
//        truck1.save()
//        def truck2 = new Truck(identifier:"truck2", model:"ELF", plateNumber:"DJF843")
//        truck2.save()
//        def truck3 = new Truck(identifier:"truck3", model:"L300", plateNumber:"TEW123")
//        truck3.save()
//
//        //PersonnelType
//        def personnelType1 = new PersonnelType(identifier:"DR",description:"Driver")
//        personnelType1.save()
//        def personnelType2 = new PersonnelType(identifier:"JD",description:"Developer")
//        personnelType2.save()
//        def personnelType3 = new PersonnelType(identifier:"WP",description:"Warehouse")
//        personnelType3.save()
//        def personnelType4 = new PersonnelType(identifier:"AC",description:"Accounting")
//        personnelType4.save()
//        def personnelType5 = new PersonnelType(identifier:"CO",description:"Corporate")
//        personnelType5.save()
//
//        //Personnel
//        def personnel1 = new Personnel(identifier:"AA", lastName:"Andrade", firstName:"Adenny",
//            type:personnelType2)
//        personnel1.save()
//        def personnel2 = new Personnel(identifier:"FC", lastName:"Co", firstName:"Ferdinand",
//            type:personnelType2)
//        personnel2.save()
//        def personnel3 = new Personnel(identifier:"CO", lastName:"Sioco", firstName:"Louie",
//            type:personnelType5)
//        personnel3.save()
//        def personnel4 = new Personnel(identifier:"AC", lastName:"Sioco", firstName:"Stephanie",
//            type:personnelType4)
//        personnel4.save()
//        def personnel5 = new Personnel(identifier:"WP",lastName:"Hardner",firstName:"Kent",
//            type:personnelType3)
//        personnel5.save()
//
//                //JobOrder
//        def jobOrder1 = new JobOrder(product:product1, startDate:new Date(), endDate:null,
//            targetDate:new Date()+7, qty:24, assignedTo:assemble1, status:"In Progress", preparedBy:"")
//        jobOrder1.save()
//        def jobOrder2 = new JobOrder(product:product4, startDate:new Date(), endDate:new Date()+14,
//            targetDate:new Date()+7, qty:35, assignedTo:assemble2, status:"Pending Confirmation", preparedBy:"")
//        jobOrder2.save()
//        def jobOrder3 = new JobOrder(product:product5, startDate:new Date(), endDate:new Date()+21,
//            targetDate:new Date()+14, qty:13, assignedTo:assemble3, status:"Awaiting Approval", preparedBy:"")
//        jobOrder3.save()
//        def jobOrder4 = new JobOrder(product:product5, startDate:new Date(), endDate:new Date()+20,
//            targetDate:new Date()+7, qty:3, assignedTo:assemble4, status:"Authorized", preparedBy:"")
//        jobOrder4.save()
//        def jobOrder5 = new JobOrder(product:product2, startDate:new Date(), endDate:null,
//            targetDate:new Date()+7, qty:9, assignedTo:assemble5, status:"Complete", preparedBy:"")
//        jobOrder5.save()
//
//        //JobOrderOuts
//        def jobOrderOut1 = new JobOrderOut(date: new Date(), qty:35, jobOrder:jobOrder1)
//        jobOrderOut1.save()
//        def jobOrderOut2 = new JobOrderOut(date: new Date(), qty:40, jobOrder:jobOrder1)
//        jobOrderOut2.save()
//        def jobOrderOut3 = new JobOrderOut(date: new Date(), qty:45, jobOrder:jobOrder1)
//        jobOrderOut3.save()
//
//        def jobOrderOut4 = new JobOrderOut(date: new Date(), qty:50, jobOrder:jobOrder2)
//        jobOrderOut4.save()
//        def jobOrderOut5 = new JobOrderOut(date: new Date(), qty:55, jobOrder:jobOrder2)
//        jobOrderOut5.save()
//        def jobOrderOut6 = new JobOrderOut(date: new Date(), qty:60, jobOrder:jobOrder2)
//        jobOrderOut6.save()
//
//        def jobOrderOut7 = new JobOrderOut(date: new Date(), qty:65, jobOrder:jobOrder3)
//        jobOrderOut7.save()
//        def jobOrderOut8 = new JobOrderOut(date: new Date(), qty:70, jobOrder:jobOrder3)
//        jobOrderOut8.save()
//        def jobOrderOut9 = new JobOrderOut(date: new Date(), qty:75, jobOrder:jobOrder3)
//        jobOrderOut9.save()
//
//        def jobOrderOut10 = new JobOrderOut(date: new Date(), qty:80, jobOrder:jobOrder4)
//        jobOrderOut10.save()
//        def jobOrderOut11 = new JobOrderOut(date: new Date(), qty:85, jobOrder:jobOrder4)
//        jobOrderOut11.save()
//
//        def jobOrderOut12 = new JobOrderOut(date: new Date(), qty:90, jobOrder:jobOrder5)
//        jobOrderOut12.save()
//        def jobOrderOut13 = new JobOrderOut(date: new Date(), qty:95, jobOrder:jobOrder5)
//        jobOrderOut13.save()
//
//        //PurchaseOrder (completed pending)
//        def po1 = new PurchaseOrder(supplier:supplier1, status:"pending", items:null, currency:currency1)
//        po1.save()
//        def po2 = new PurchaseOrder(supplier:supplier2, status:"completed", items:null, currency:currency2)
//        po2.save()
//        def po3 = new PurchaseOrder(supplier:supplier3, status:"pending", items:null, currency:currency3)
//        po3.save()
//
//        //PurchaseOrderItem
//        def purchaseItem1 = new PurchaseOrderItem(product:product1, qty:"250", price:"256.75", po:po1)
//        purchaseItem1.save()
//        def purchaseItem2 = new PurchaseOrderItem(product:product3, qty:"500", price:"135.50", po:po1)
//        purchaseItem2.save()
//        def purchaseItem3 = new PurchaseOrderItem(product:product4, qty:"300", price:"145", po:po1)
//        purchaseItem3.save()
//
//        def purchaseItem4 = new PurchaseOrderItem(product:product2, qty:"150", price:"120.75", po:po3)
//        purchaseItem4.save()
//        def purchaseItem5 = new PurchaseOrderItem(product:product5, qty:"375", price:"320.50", po:po3)
//        purchaseItem5.save()
//        def purchaseItem6 = new PurchaseOrderItem(product:product3, qty:"50", price:"135.50", po:po3)
//        purchaseItem6.save()
//
//        def purchaseItem7 = new PurchaseOrderItem(product:product1, qty:"1000", price:"256.75", po:po2)
//        purchaseItem7.save()
//        def purchaseItem8 = new PurchaseOrderItem(product:product4, qty:"500", price:"145", po:po2)
//        purchaseItem8.save()
//        def purchaseItem9 = new PurchaseOrderItem(product:product3, qty:"400", price:"135.50", po:po2)
//        purchaseItem9.save()
//
//        //PurchaseDelivery
//        def purchaseDelivery1 = new PurchaseDelivery(reference:"Reference", po:po1)
//        purchaseDelivery1.save()
//        def purchaseDelivery2 = new PurchaseDelivery(reference:"Reference 2", po:po2)
//        purchaseDelivery2.save()
//        def purchaseDelivery3 = new PurchaseDelivery(reference:"Reference 3", po:po3)
//        purchaseDelivery3.save()
//
//        //PurchaseDeliveryItem
//        def purchaseDeliveryItem1 = new PurchaseDeliveryItem(product:product5, qty:2465,
//            delivery:purchaseDelivery1)
//        purchaseDeliveryItem1.save()
//        def purchaseDeliveryItem2 = new PurchaseDeliveryItem(product:product4, qty:516,
//            delivery:purchaseDelivery1)
//        purchaseDeliveryItem2.save()
//        def purchaseDeliveryItem3 = new PurchaseDeliveryItem(product:product2, qty:2462672,
//            delivery:purchaseDelivery1)
//        purchaseDeliveryItem3.save()
//
//        def purchaseDeliveryItem4 = new PurchaseDeliveryItem(product:product3, qty:26,
//            delivery:purchaseDelivery2)
//        purchaseDeliveryItem4.save()
//        def purchaseDeliveryItem5 = new PurchaseDeliveryItem(product:product4, qty:373788,
//            delivery:purchaseDelivery2)
//        purchaseDeliveryItem5.save()
//        def purchaseDeliveryItem6 = new PurchaseDeliveryItem(product:product1, qty:426,
//            delivery:purchaseDelivery2)
//        purchaseDeliveryItem6.save()
//
//        def purchaseDeliveryItem7 = new PurchaseDeliveryItem(product:product1, qty:99,
//            delivery:purchaseDelivery3)
//        purchaseDeliveryItem7.save()
//        def purchaseDeliveryItem8 = new PurchaseDeliveryItem(product:product2, qty:98,
//            delivery:purchaseDelivery3)
//        purchaseDeliveryItem8.save()
//        def purchaseDeliveryItem9 = new PurchaseDeliveryItem(product:product3, qty:9753,
//            delivery:purchaseDelivery3)
//        purchaseDeliveryItem9.save()
//
//        //SupplierItem
//        def supplierItem1 = new SupplierItem(productCode:"eh1001", product:product1, cost:"134",
//            supplier:supplier1)
//        supplierItem1.save()
//        def supplierItem2 = new SupplierItem(productCode:"ghj76", product:product5, cost:"100",
//            supplier:supplier5)
//        supplierItem2.save()
//        def supplierItem3 = new SupplierItem(productCode:"ndh83", product:product4, cost:"234",
//            supplier:supplier2)
//        supplierItem3.save()
//        def supplierItem4 = new SupplierItem(productCode:"1378rh", product:product3, cost:"105",
//            supplier:supplier3)
//        supplierItem4.save()
//        def supplierItem5 = new SupplierItem(productCode:"bdh78", product:product2, cost:"145",
//            supplier:supplier4)
//        supplierItem5.save()
//
//        //SalesInvoice
//        def salesInvoice1 = new SalesInvoice(date:new Date(),
//            deliveryDate:new Date()+7, remark:"Complete", status:"Closed",warehouse:warehouse1,type:invoiceType1)
//        salesInvoice1.save()
//        def salesInvoice2 = new SalesInvoice(date:new Date(),
//            deliveryDate:new Date()+7, remark:"Incomplete", status:"On hold",warehouse:warehouse2,type:invoiceType3)
//        salesInvoice2.save()
//        def salesInvoice3 = new SalesInvoice(date:new Date(),
//            deliveryDate:new Date()+7, remark:"Cancelled", status:"Pending",warehouse:warehouse3,type:invoiceType2)
//        salesInvoice3.save()
//
//        //SalesInvoiceItem
//        def salesInvoiceItem1 = new SalesInvoiceItem(product:product1, qty:25, price:500, invoice:salesInvoice1)
//        salesInvoiceItem1.save()
//        def salesInvoiceItem2 = new SalesInvoiceItem(product:product2, qty:12, price:890, invoice:salesInvoice1)
//        salesInvoiceItem2.save()
//        def salesInvoiceItem3 = new SalesInvoiceItem(product:product3, qty:246, price:1000, invoice:salesInvoice1)
//        salesInvoiceItem3.save()
//
//        def salesInvoiceItem4 = new SalesInvoiceItem(product:product1, qty:25, price:4627, invoice:salesInvoice2)
//        salesInvoiceItem4.save()
//        def salesInvoiceItem5 = new SalesInvoiceItem(product:product3, qty:12, price:262, invoice:salesInvoice2)
//        salesInvoiceItem5.save()
//        def salesInvoiceItem6 = new SalesInvoiceItem(product:product5, qty:246, price:372, invoice:salesInvoice2)
//        salesInvoiceItem6.save()
//
//        def salesInvoiceItem7 = new SalesInvoiceItem(product:product2, qty:15, price:377, invoice:salesInvoice3)
//        salesInvoiceItem7.save()
//        def salesInvoiceItem8 = new SalesInvoiceItem(product:product3, qty:13, price:274, invoice:salesInvoice3)
//        salesInvoiceItem8.save()
//        def salesInvoiceItem9 = new SalesInvoiceItem(product:product4, qty:51, price:654, invoice:salesInvoice3)
//        salesInvoiceItem9.save()
//
//        //Stock
//        def stock1 = new Stock(warehouse:warehouse2, qty:"100", product:product1)
//        stock1.save()
//        def stock2 = new Stock(warehouse:warehouse2, qty:"100", product:product2)
//        stock2.save()
//        def stock3 = new Stock(warehouse:warehouse2, qty:"100", product:product3)
//        stock3.save()
//        def stock4 = new Stock(warehouse:warehouse2, qty:"100", product:product4)
//        stock4.save()
//        def stock5 = new Stock(warehouse:warehouse2, qty:"100", product:product5)
//        stock5.save()
//
//        //StockTranfer
//        def stockTransfer1 = new StockTransfer(date:new Date(), originWarehouse:warehouse2,
//            destinationWarehouse:warehouse1, status:"Complete", items:null, preparedBy:"")
//        stockTransfer1.save()
//        def stockTransfer2 = new StockTransfer(date:new Date()-1, originWarehouse:warehouse2,
//            destinationWarehouse:warehouse3, status:"In-Transit", items:null, preparedBy:"")
//        stockTransfer2.save()
//        def stockTransfer3 = new StockTransfer(date:new Date()-2, originWarehouse:warehouse2,
//            destinationWarehouse:warehouse4, status:"Incomplete", items:null, preparedBy:"")
//        stockTransfer3.save()
//
//        //StockTransferItem
//        def stockTransferItem1 = new StockTransferItem(product:product1, qty:8, receivedQty:8,
//            transfer:stockTransfer1)
//        stockTransferItem1.save()
//        def stockTransferItem2 = new StockTransferItem(product:product3, qty:13, receivedQty:13,
//            transfer:stockTransfer1)
//        stockTransferItem2.save()
//        def stockTransferItem3 = new StockTransferItem(product:product4, qty:25, receivedQty:25,
//            transfer:stockTransfer1)
//        stockTransferItem3.save()
//
//        def stockTransferItem4 = new StockTransferItem(product:product1, qty:6, receivedQty:6,
//            transfer:stockTransfer2)
//        stockTransferItem4.save()
//        def stockTransferItem5 = new StockTransferItem(product:product3, qty:12, receivedQty:12,
//            transfer:stockTransfer2)
//        stockTransferItem5.save()
//        def stockTransferItem6 = new StockTransferItem(product:product4, qty:5, receivedQty:5,
//            transfer:stockTransfer2)
//        stockTransferItem6.save()
//
//        def stockTransferItem7 = new StockTransferItem(product:product2, qty:20, receivedQty:20,
//            transfer:stockTransfer3)
//        stockTransferItem7.save()
//        def stockTransferItem8 = new StockTransferItem(product:product5, qty:30, receivedQty:30,
//            transfer:stockTransfer3)
//        stockTransferItem8.save()
//        def stockTransferItem9 = new StockTransferItem(product:product4, qty:15, receivedQty:15,
//            transfer:stockTransfer3)
//        stockTransferItem9.save()
    }
    def destroy = {
    }
}
