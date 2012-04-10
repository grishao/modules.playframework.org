/*
 * Copyright 2012 The Play! Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package controllers;

import actions.CurrentUser;
import models.Category;
import models.Module;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import views.html.admin.categories;
import views.html.admin.categoryDetails;

import java.util.List;

import static actions.CurrentUser.currentUser;

/**
 * @author Steve Chaloner (steve@obectify.be)
 */
@With(CurrentUser.class)
public class Categories extends Controller
{
    public static Result showCategories()
    {
        return ok(categories.render(CurrentUser.currentUser(),
                                    Category.getAll(),
                                    form(Category.class)));
    }

    public static Result addNewCategory()
    {
        Result result;
        Form<Category> form = form(Category.class).bindFromRequest();
        if (form.hasErrors())
        {
            result = badRequest(categories.render(currentUser(),
                                                  Category.getAll(),
                                                  form));
        }
        else
        {
            Category category = form.get();
            category.save();
            result = showCategories();
        }
        return result;
    }

    public static Result categoryDetails(String name)
    {
        Category category = Category.findByName(name);
        List<Module> modules = Module.findByCategory(category);
        return ok(categoryDetails.render(currentUser(),
                                         category,
                                         modules));
    }
}
