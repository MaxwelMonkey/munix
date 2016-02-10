class UrlMappings {
    static mappings = {
      "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }

      "/index"{
            view = "index"
        }
      "/"(controller:"home", action:"index")
      "/index.gsp"(controller:"home", action:"index")
	  "500"(view:'/error')
    }
}
