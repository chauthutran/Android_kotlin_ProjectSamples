import connectMongoDB from "@/app/libs/mongodb";
import Topic from "@/app/module/topic";
import { NextResponse } from "next/server";

export async function PUT( request, {params}) {
    const { id } = params;
    const {newTitle: title, newDescription: description} = await request.json();
    await connectMongoDB();
    await Topic.findByIdAndUpdate( id, {title, description});
    return NextResponse.json({message: "Topic updated"}, {status: 200});
}

export async function GET( request, {params}) {
    const { id } = params;
    await connectMongoDB();
    // const topic = await Topic.findOne({_id: id});
    const topic = await Topic.findById(id);
    return NextResponse.json({topic}, {status: 200});
}

export const dynamic = "force-dynamic";